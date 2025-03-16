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

package de.bwl.bwfla.emucomp;


import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;
import de.bwl.bwfla.emucomp.components.emulators.EmulatorBean;
import de.bwl.bwfla.emucomp.components.network.NetworkSwitchBean;
import de.bwl.bwfla.emucomp.components.network.NodeTcpBean;
import de.bwl.bwfla.emucomp.components.network.VdeSlirpBean;
import de.bwl.bwfla.emucomp.components.network.VdeSocksBean;
import de.bwl.bwfla.emucomp.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.logging.PrefixLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.tamaya.inject.api.Config;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@ApplicationScoped
public class NodeManager {
    @Inject
    protected PrefixLogger plog;

    @Inject
    @Resource
    protected ManagedThreadFactory workerThreadFactory;

    @Inject
    @Resource
    protected ManagedScheduledExecutorService scheduler;

    @Inject
    @Resource
    protected ManagedExecutor executor;

    protected AbstractEaasComponent currentComponent;

    private final ThreadLocal<ComponentConfiguration> usedComponentConfiguration = ThreadLocal.withInitial(() -> null);

    @Inject
    @Config("components.warmup_timeout")
    protected Duration componentWarmupTimeout;

    @Inject
    @Config("components.timeout")
    protected Duration componentExpirationTimeout;


    // TODO: does it make sense to do something for @PreDestroy?


    public ManagedThreadFactory getWorkerThreadFactory() {
        return workerThreadFactory;
    }

    /**
     * Creates and registers a component with the given {@componentId} and
     * configuration.
     * <p>
     * After this method is called, the instance is allocated, under proper
     * resource management and fully initialized.
     *
     * @param componentId
     * @param config      a serialized instance of a {@link ComponentConfiguration}
     * @return the given {@code componentId}
     * @throws BWFLAException if {@code config} could not be unmarshalled or a
     *                        runtime error occurred while registering the component to the
     *                        local component map or an exception from
     *                        {@link createComponentInstance}
     * @see #createComponentInstance(ComponentConfiguration, String)
     */
    public String allocateComponent(String componentId, String config) throws BWFLAException {
        try {
            final ComponentConfiguration configuration =
                    ComponentConfiguration.fromValue(config, ComponentConfiguration.class);

            // atomically create a new bean iff the given id does not already exist
            final Supplier<AbstractEaasComponent> component = () -> {
                if (usedComponentConfiguration.get() != null && Objects.deepEquals(usedComponentConfiguration, configuration)) {
                    log.info("Already allocated component: {}", componentId);
                } else {
                    usedComponentConfiguration.set(configuration);
                    try {
                        return createComponentInstance(configuration, componentId);
                    } catch (BWFLAException e) {
                        throw new RuntimeException(e);
                    }
                }
                throw new RuntimeException("Component " + componentId + " already allocated");
            };


            // don't to this in the atomic insert to reduce
            // the time the map's bucket is locked

            currentComponent = component.get();
            currentComponent.initialize(configuration);

            return componentId;

        } catch (RuntimeException e) {
            // from the computeIfAbsent functor
            if (e.getCause() instanceof BWFLAException) {
                throw (BWFLAException) e.getCause();
            } else {
                throw new BWFLAException("A runtime error occurred while allocating the component: " + e.getMessage(), e);
            }
        } catch (BWFLAException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Destroys the component instance with the given {@code componentId}.
     *
     * @Removed
     * @param componentId
     */
    public void releaseComponent() {
        if (currentComponent != null)
            currentComponent.destroy();
    }

    /**
     * Resets the keepalive timeout for the specified {@code component}.
     *
     * @param componentId
     */
    public void keepalive(String componentId) throws BWFLAException {
        AbstractEaasComponent component = this.getComponentById(componentId, AbstractEaasComponent.class);
        component.setKeepaliveTimestamp(NodeManager.timestamp());
    }

    /**
     * Resets the keepalive timeout for the specified {@code component}.
     *
     */
    public void keepalive() throws BWFLAException {
        AbstractEaasComponent component = this.getCurrentComponent();
        component.setKeepaliveTimestamp(NodeManager.timestamp());
    }


//    /**
//     * Returns the component instance for the given {@code componentId}.
//     *
//     * @param componentId
//     * @return a component instance
//     * @throws BWFLAException if there is no registered component instance with
//     *                        the given id.
//     */
    public AbstractEaasComponent getComponentById(String componentId) throws BWFLAException {
        AbstractEaasComponent component = this.currentComponent;
        if (component == null) {
            throw new BWFLAException("Could not find a component instance for the given id: " + componentId);
        }

        return component;
    }

    public AbstractEaasComponent getCurrentComponent() throws BWFLAException {
        if(currentComponent == null) {
            throw new BWFLAException("Component is not allocated and cannot be found.");
        }
        return currentComponent;
    }

    @Deprecated
    public <T> T getComponentById(String componentId, Class<T> klass) throws BWFLAException {
        return klass.cast(this.getCurrentComponent());
    }

    public <T> T getComponentTransformed(Class<T> klass) throws BWFLAException {
        return klass.cast(this.getCurrentComponent());
    }


    /**
     * Creates a component instance depending on the type of the
     * {@code configuration} argument.
     * <p>
     * The returned components are bare CDI-enabled instances and are not yet
     * configured or initialized.
     *
     * @param configuration the configuration to base the instantiation on
     * @param componentId   a component id to give to the instance
     * @return A component instance
     * @throws BWFLAException if the required bean type cannot be found by the
     *                        classloader or the configuration does not correspond to any
     *                        known bean class.
     */
    protected AbstractEaasComponent createComponentInstance(ComponentConfiguration configuration, String componentId) throws BWFLAException {
        try {
            AbstractEaasComponent component;

            if (configuration instanceof MachineConfiguration) {
                component = EmulatorBean.createEmulatorBean((MachineConfiguration) configuration);
                component.setEnvironmentId(((MachineConfiguration) configuration).getId());
            }
//            else if (configuration instanceof ContainerConfiguration) {
//                component = ContainerBean.createContainerBean((ContainerConfiguration) configuration);
//            }
            else if (configuration instanceof VdeSlirpConfiguration) {
                component = VdeSlirpBean.createVdeSlirp((VdeSlirpConfiguration) configuration);
            } else if (configuration instanceof NetworkSwitchConfiguration) {
                component = NetworkSwitchBean.createNetworkSwitch((NetworkSwitchConfiguration) configuration);
            } else if (configuration instanceof VdeSocksConfiguration) {
                component = VdeSocksBean.createVdeSocks((VdeSocksConfiguration) configuration);
            } else if (configuration instanceof NodeTcpConfiguration) {
                component = NodeTcpBean.createNodeTcp((NodeTcpConfiguration) configuration);
            } else {
                throw new BWFLAException("(Valid) Configuration does not correspond to a component type. This is almost certainly a programming error!");
            }

            component.setComponentId(componentId);
            component.setKeepaliveTimestamp(NodeManager.timestamp());

            // Submit cleanup handler
            final Runnable cleanup = new CleanupTrigger(component, componentExpirationTimeout);
            scheduler.schedule(cleanup, componentWarmupTimeout.toMillis(), TimeUnit.MILLISECONDS);

            return component;

        } catch (ClassNotFoundException e) {
            throw new BWFLAException("The requested bean type could not be found.", e);
        }
    }

    protected void onComponentTimeout() {
        if (currentComponent == null)
            return;

        log.info("Aww, component " + currentComponent.getComponentId() + " has timed out :-(");
        this.releaseComponent();
    }

    private static long timestamp() {
        return System.currentTimeMillis();
    }

    private class CleanupTrigger implements Runnable {
        private final AbstractEaasComponent component;
        private final long timeout;

        public CleanupTrigger(AbstractEaasComponent component, Duration timeout) {
            this.component = component;
            this.timeout = timeout.toMillis();
        }

        @Override
        public void run() {
            final long curts = NodeManager.timestamp();
            final long prevts = component.getKeepaliveTimestamp();
            final long elapsed = curts - prevts;
            if (elapsed < timeout) {
                // Component should be kept alive! Schedule this task again.
                final long delay = timeout - elapsed + 10L;
                scheduler.schedule(this, delay, TimeUnit.MILLISECONDS);
            } else {
                // Timeout expired!

                // Since scheduler tasks should complete quickly and this.onComponentTimeout()
                // can take longer, submit a new task to an unscheduled executor for it.
                executor.execute(NodeManager.this::onComponentTimeout);
            }
        }
    }
}
