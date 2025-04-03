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

package de.bwl.bwfla.emucomp.control.connectors;

import de.bwl.bwfla.emucomp.api.EthernetAlreadyConnectedException;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.common.utils.ProcessRunner;
import de.bwl.bwfla.emucomp.components.emulators.EmulatorBean;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;


public class EthernetConnector implements IConnector {
    public final static String PROTOCOL = "ws+ethernet";

    private final Logger log;
    private final EmulatorBean emubean;
    private final String hwAddress;
    private final Path vdeSocket;
    private ProcessRunner runner = null;

    static {
        final var workdir = Path.of("/tmp/eaas/vde/");
        try {
            Files.createDirectories(workdir);
        }
        catch (Exception error) {
            throw new RuntimeException("Creating working directory for ethernet-connectors failed!", error);
        }
    }
    
    public static String getProtocolForHwaddress(final String hwAddress) {
        return EthernetConnector.PROTOCOL + "+" + hwAddress;
    }

    public EthernetConnector(final String hwAddress, final Path vdeSocket, Logger log) {
        this(hwAddress, vdeSocket, log, null);
    }

    public EthernetConnector(final String hwAddress, final Path vdeSocket, Logger log, EmulatorBean emubean) {
        this.log = log;
        this.emubean = emubean;
        this.hwAddress = hwAddress;
        this.vdeSocket = vdeSocket;
    }

    @Override
    public URI getControlPath(URI componentResource) {
        return URI.create("ws://hostname/" + componentResource.resolve(EthernetConnector.PROTOCOL + "/" + hwAddress));
    }
    
    @Override
    public String getProtocol() {
        return getProtocolForHwaddress(this.hwAddress);
    }

    public synchronized String connect(String id)
            throws EthernetAlreadyConnectedException, BWFLAException
    {
        log.info("Starting ethernet-connector for '" + hwAddress + "'...");
        if (this.runner != null)
            throw new EthernetAlreadyConnectedException();

        final var sockpath = "/tmp/eaas/vde/" + id + ".sock";

        // Start a new VDE plug instance that connects to the emulator's switch
        this.runner = new ProcessRunner();
        runner.setLogger(log);
        runner.setCommand("socat");
        runner.addArgument("unix-listen:" + sockpath);

        String socatExec = "exec ";
        if (emubean != null && emubean.isContainerModeEnabled()) {
            socatExec += "sudo runc exec --user "
                    + emubean.getContainerUserId() + ":" + emubean.getContainerGroupId() + " " + emubean.getContainerId()  + " ";
        }
        socatExec += "vde_plug " +  this.vdeSocket.toString();

        runner.addEnvVariable("SOCATCMD", socatExec);
        runner.addArgument("exec:sh -c $SOCATCMD");
        if (!runner.start())
            throw new BWFLAException("Running emulator-side vde-plug failed!");

        log.info("Started ethernet-connector for '" + hwAddress + "' (" + sockpath + "')");
        return sockpath;
    }
    
    public void close() {
        if (runner == null)
            return;

        try {
            runner.stop();
            runner.printStdOut();
            runner.printStdErr();
        }
        finally {
            runner.cleanup();
            this.runner = null;
        }

        log.info("Stopped ethernet-connector for '" + hwAddress + "'");
    }
}
