/*
 * This file is part of the Emulation-as-a-Service framework.
 *
 * The Emulation-as-a-Service framework is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Emulation-as-a-Service framework is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Emulation-as-a-Software framework.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package de.bwl.bwfla.components.containers;

import de.bwl.bwfla.blob.BlobDescription;
import de.bwl.bwfla.blob.BlobHandle;
import de.bwl.bwfla.components.BindingsManager;
import de.bwl.bwfla.components.EaasComponentBean;
import de.bwl.bwfla.config.ComponentConfiguration;
import de.bwl.bwfla.config.ComponentsConfig;
import de.bwl.bwfla.config.ContainerConfiguration;
import de.bwl.bwfla.config.DockerContainerConfiguration;
import de.bwl.bwfla.config.bindings.AbstractDataResource;
import de.bwl.bwfla.enums.ComponentState;
import de.bwl.bwfla.exceptions.BWFLAException;
import de.bwl.bwfla.impl.ContainerComponent;
import de.bwl.bwfla.runner.DeprecatedProcessRunner;
import de.bwl.bwfla.util.EmulatorUtils;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Slf4j
public abstract class DeploymentBean extends EaasComponentBean implements ContainerComponent {
    protected final ContainerBeanState conBeanState = new ContainerBeanState(ContainerState.UNDEFINED);

    protected ContainerConfiguration config;

    protected final DeprecatedProcessRunner conRunner = new DeprecatedProcessRunner();

    protected final BindingsManager bindings = new BindingsManager();

    @Resource(lookup = "java:jboss/ee/concurrency/factory/default")
    protected ManagedThreadFactory workerThreadFactory;

    @Inject
    protected ComponentsConfig componentsConfig;

    public static DeploymentBean createContainerBean(ContainerConfiguration config) throws ClassNotFoundException {
        Class<?> clazz = null;
        if (config instanceof DockerContainerConfiguration)
            clazz = DockerBean.class;
        else
            throw new ClassNotFoundException("Unsupported container configuration type: " + config.getClass().getName());

        return (DeploymentBean) CDI.current().select(clazz).get();
    }

    public Path getDataDir() {
        return this.getWorkingDir().resolve("data");
    }

    public Path getBindingsDir() {
        return this.getDataDir().resolve("bindings");
    }

    public Path getOutputDir() {
        return this.getDataDir().resolve("output");
    }

    @Override
    public String getComponentType() {
        return "container";
    }

    @Override
    public ComponentState getState() {
        final ContainerState state = conBeanState.fetch();
        switch (state) {
            case FAILED:
                return ComponentState.FAILED;

            case STOPPED:
                return ComponentState.STOPPED;

            default:
                return ComponentState.OK;
        }
    }

    public static void sync() throws BWFLAException {
        final DeprecatedProcessRunner process = new DeprecatedProcessRunner();
        process.setCommand("sync");
        if (!process.execute())
            throw new BWFLAException("Syncing filesystem failed!");
    }


    @Override
    public void initialize(ComponentConfiguration compConfig) throws BWFLAException {
        synchronized (conBeanState) {
            final ContainerState curstate = conBeanState.get();
            if (curstate != ContainerState.UNDEFINED)
                this.abort("Cannot initialize ContainerBean! Wrong state detected: " + curstate.value());

            conBeanState.set(ContainerState.BUSY);
        }

        try {
            this.createWorkingSubDirs();
        } catch (IOException error) {
            this.fail("Creating working subdirs failed!", error);
        }

        conRunner.setLogger(Logger.getLogger(DeploymentBean.class.getName()));

        try {
            this.config = (ContainerConfiguration) compConfig;
        } catch (Exception error) {
            this.fail("Parsing container's configuration failed!", error);
        }

        try {
            // Register all specified resources
            for (AbstractDataResource resource : config.getDataResources())
                bindings.register(resource);

            final EmulatorUtils.XmountOutputFormat format = EmulatorUtils.XmountOutputFormat.RAW;
            final Path outdir = this.getBindingsDir();

            // Resolve and mount all bindings
            if (config.hasInputs()) {
                for (ContainerConfiguration.Input input : config.getInputs())
                    bindings.mount(input.getBinding(), outdir, format);
            }

            this.prepare();
        } catch (Exception error) {
            this.fail("Preparing container's resources failed!", error);
            error.printStackTrace();
        }

        final String compid = this.getComponentId();
        log.info("Container session '" + compid + "' initialized");
        log.info("Working directory for session '" + compid + "' created at: " + this.getWorkingDir());
        conBeanState.update(ContainerState.READY);
    }

    @Override
    public void start() throws BWFLAException {
        synchronized (conBeanState) {
            final ContainerState curstate = conBeanState.get();
            if (curstate != ContainerState.READY && curstate != ContainerState.STOPPED)
                this.abort("Cannot start container! Wrong state detected: " + curstate.value());

            conBeanState.set(ContainerState.BUSY);
        }

        if (!conRunner.start())
            this.fail("Starting container failed!");

        log.info("Container started in process " + conRunner.getProcessId());

        final Thread conObserver = workerThreadFactory.newThread(() -> {

                    conRunner.waitUntilFinished();

                    log.info("Preparing container's output...");
                    try {
                        final String name = "output";
                        final String extention = ".tar.gz";

                        final Path outdir = this.getOutputDir();
                        final Path workdir = this.getWorkingDir();
                        final Path archive = workdir.resolve(name + extention);

                        // Always include container's stdout/err to output archive!
                        DeploymentBean.copy(conRunner.getStdOutPath(), outdir);
                        DeploymentBean.copy(conRunner.getStdErrPath(), outdir);

                        // Create an archive file
                        final DeprecatedProcessRunner tar = new DeprecatedProcessRunner("tar");
                        tar.addArguments("--create", "--auto-compress", "--totals");
                        tar.addArguments("--file", archive.toString());
                        tar.addArguments("--directory", outdir.toString());
                        try (Stream<Path> stream = Files.list(outdir)) {
                            // Add each file to the archive...
                            stream.forEach((path) -> {
                                final Path file = path.getFileName();
                                tar.addArgument(file.toString());
                            });
                        }

                        sync();

                        tar.setWorkingDirectory(outdir);
                        tar.setLogger(Logger.getLogger(DeploymentBean.class.getName()));
                        if (!tar.execute())
                            throw new IOException("Creating output archive failed!");

                        log.info("Uploading container's output to blobstore at " + componentsConfig.getContainers().getBlobstore() + "...");

                        // Create a BLOB description for the created archive
                        final BlobDescription blob = new BlobDescription()
                                .setDescription("Container's output for session " + this.getComponentId())
                                .setNamespace("container-outputs")
                                .setAccessToken(UUID.randomUUID().toString())
                                .setDataFromFile(archive)
                                .setType(extention)
                                .setName(name);

                        // Upload the archive to the BlobStore
                        final BlobHandle handle = BlobStoreClient.get()
                                .getBlobStorePort(componentsConfig.getContainers().getBlobstore())
                                .put(blob);

                        log.info("Container's output uploaded to blobstore");

                        result.complete(handle);
                    } catch (Exception error) {
                        this.failNoThrow("Preparing container's output failed!\n", error);
                        return;
                    }

                    // cleanup will be performed later by ContainerBean.destroy()

                    synchronized (conBeanState) {
                        final ContainerState curstate = conBeanState.get();
                        if (curstate != ContainerState.RUNNING)
                            return;

                        if (conRunner.getReturnCode() == 0) {
                            log.info("Container stopped normally");
                            conBeanState.set(ContainerState.STOPPED);
                        } else {
                            log.warn("Container stopped unexpectedly, returning code: " + conRunner.getReturnCode());
                            conBeanState.set(ContainerState.FAILED);
                        }
                    }
                }
        );

        conObserver.start();
        conBeanState.update(ContainerState.RUNNING);
    }

    @Override
    public void stop() throws BWFLAException {
        synchronized (conBeanState) {
            final ContainerState curstate = conBeanState.get();
            if (curstate != ContainerState.RUNNING)
                this.abort("Cannot stop container! Wrong state detected: " + curstate.value());

            conBeanState.set(ContainerState.BUSY);
        }

        this.stopInternal();

        conBeanState.update(ContainerState.STOPPED);
    }

    @Override
    public void destroy() {
        synchronized (conBeanState) {
            final ContainerState curstate = conBeanState.get();
            if (curstate == ContainerState.UNDEFINED)
                return;

            if (curstate == ContainerState.BUSY) {
                log.error("Destroying ContainerBean while other operation is in-flight!");
                return;
            }

            conBeanState.set(ContainerState.UNDEFINED);
        }

        this.stopInternal();
        bindings.cleanup();

        // Cleanup container's runner here
        conRunner.printStdOut();
        conRunner.printStdErr();
        conRunner.cleanup();

        {
            DeprecatedProcessRunner runner = new DeprecatedProcessRunner("sudo");
            runner.addArguments("--non-interactive", "--", "rm", "-r", "-f");
            runner.addArgument(this.getOutputDir().toString());
            if (!runner.execute()) {
                log.warn("Deleting of output dir failed!");
            }
        }

        log.info("ContainerBean for session " + this.getComponentId() + " destroyed.");

        // Destroy base class!
        super.destroy();

        // Collect garbage
        System.gc();
    }


    /* =============== Internal Helpers =============== */

    protected void abort(String message) throws BWFLAException {
        log.warn(message);
        throw new BWFLAException(message);
    }

    protected void abort(String message, Exception error) throws BWFLAException {
        log.warn(message + "\n", error);
        throw new BWFLAException(message, error);
    }

    protected void fail(String message) throws BWFLAException {
        conBeanState.update(ContainerState.FAILED);
        this.abort(message);
    }

    protected void fail(String message, Exception error) throws BWFLAException {
        conBeanState.update(ContainerState.FAILED);
        this.abort(message, error);
    }

    protected void failNoThrow(String message, Exception error) {
        conBeanState.update(ContainerState.FAILED);
        log.warn(message + "\n", error);
    }

    protected String getContainerId() {
        final String compid = this.getComponentId();
        return compid.substring(1 + compid.lastIndexOf("+"));
    }

    protected String getContainerRuntimeUser() {
        return componentsConfig.getContainers().getUsernamespace().getUser();
    }

    protected String getContainerRuntimeGroup() {
        return componentsConfig.getContainers().getUsernamespace().getGroup();
    }

    protected boolean isUserNamespaceEnabled() {
        return componentsConfig.getContainers().getUsernamespace().isEnabled();
    }

    /**
     * Prepare container runtime. Should be overridden by subclasses.
     */
    protected void prepare() throws Exception {
        // Do nothing!
    }

    private void createWorkingSubDirs() throws IOException {
        // Currently, working directory is structured as follows:
        //
        // <workdir>/
        //     data/           -> Session specific data
        //         bindings/   -> Object/image bindings
        //         output/     -> Output files

        Files.createDirectories(this.getDataDir());
        Files.createDirectories(this.getBindingsDir());
        Files.createDirectories(this.getOutputDir());
    }

    private void stopInternal() {
        if (conRunner.isProcessRunning()) {
            final String cid = this.getContainerId();
            log.info("Stopping container " + cid + "...");
            conRunner.stop();
            log.info("Container " + cid + " stopped");
        }
    }

    private static void copy(Path source, Path dstdir, CopyOption... options) throws IOException {
        Files.copy(source, dstdir.resolve(source.getFileName()), options);
    }
}
