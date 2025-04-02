package de.bwl.bwfla.emucomp.components.network;

import de.bwl.bwfla.common.exceptions.BWFLAException;
import de.bwl.bwfla.common.utils.NetworkUtils;
import de.bwl.bwfla.common.utils.ProcessRunner;
import de.bwl.bwfla.common.utils.net.ConfigKey;
import de.bwl.bwfla.common.utils.net.PortRangeProvider;
import de.bwl.bwfla.emucomp.api.ComponentConfiguration;
import de.bwl.bwfla.emucomp.api.NodeTcpConfiguration;
import de.bwl.bwfla.emucomp.components.EaasComponentBean;
import de.bwl.bwfla.emucomp.control.connectors.EthernetConnector;
import de.bwl.bwfla.emucomp.control.connectors.InfoDummyConnector;
import org.apache.tamaya.inject.api.Config;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;


public class NodeTcpBean extends EaasComponentBean {

    protected ProcessRunner runner = new ProcessRunner();
    private ArrayList<ProcessRunner> vdeProcesses = new ArrayList<ProcessRunner>();

    @Inject
    @ConfigKey("components.tcpNode.ports")
    private PortRangeProvider.Port tcpPorts;

    @Inject
    @Config("components.binary.nodetcprunner")
    private String nodeTcpRunner;

    @Inject
    @Config("components.binary.nodetcpscript")
    private String nodeTcpScript;

    @Override
    public void destroy() {
        LOG.info("Stopping node-tcp instance...");
        while (!vdeProcesses.isEmpty()) {
            final var process = vdeProcesses.remove(vdeProcesses.size() - 1);
            try {
                process.stop();
                process.printStdOut();
                process.printStdErr();
            }
            catch (Throwable error) {
                LOG.log(Level.WARNING, "Stopping subprocess failed!", error);
            }
            finally {
                process.cleanup();
            }
        }

        tcpPorts.release();
        super.destroy();

        LOG.info("Stopped node-tcp instance");
    }

    @Override
    public void initialize(ComponentConfiguration config) throws BWFLAException {

        LOG.info("Initializing node-tcp instance...");

        NodeTcpConfiguration nodeConfig = (NodeTcpConfiguration) config;

        String hwAddress = nodeConfig.getHwAddress();
        String switchName = "nic_" + hwAddress;
        final var vdeSocketsPath = this.getWorkingDir()
                .resolve(switchName);

        int extPort;
        try {
             extPort = tcpPorts.get();
             LOG.info("Connection on port: " + extPort);
        } catch (IOException e) {
            throw new BWFLAException(e);
        }

        ProcessRunner process = new ProcessRunner("vde_switch");
        process.addArgument("-hub");
        process.addArgument("-s");
        process.addArgument(vdeSocketsPath.toString());
        process.setLogger(LOG);
        if (!process.start(false))
            throw new BWFLAException("Cannot create vde_switch hub for VdeSlirpBean");
        vdeProcesses.add(process);

        runner.setCommand(nodeTcpRunner);
        String info = null;
        if(nodeConfig.isDhcp())     // DCHCPD hack
        {
            // Usage: ./eaas-proxy "" /tmp/switch1 "" 10.0.0.1/24 dhcpd
            runner.addArgument("");
            runner.addArgument(vdeSocketsPath.toString());
            runner.addArgument("");

            runner.addArgument(nodeConfig.getDhcpNetworkAddress() + "/" + nodeConfig.getDhcpNetworkMask());
            runner.addArgument("dhcpd");
        }
        else {
            // arg1 extPort
            // arg2 wsURL
            // arg3 randomMac
            // arg4 privateNetworkIp (internal)/24
            // arg5 privateDestIp (internal server)
            // arg6 privateDestIpPort
            runner.addArgument(extPort + "");
            runner.addArgument(vdeSocketsPath.toString());
            runner.addArgument(NetworkUtils.getRandomHWAddress());
            runner.addArgument("dhcp");
            // runner.addArgument(nodeConfig.getPrivateNetIp() + "/" + nodeConfig.getPrivateNetMask());

            if (nodeConfig.isSocksMode()) {
                String sockString = "socks5";
                info = "socks/" + extPort;
                if (nodeConfig.getSocksUser() != null && nodeConfig.getSocksPasswd() != null) {
                    sockString += ":" + nodeConfig.getSocksUser() + ":" + nodeConfig.getSocksPasswd();
                    info += "/" + nodeConfig.getSocksUser() + "/" + nodeConfig.getSocksPasswd();
                }
                runner.addArgument(sockString);
            } else if (nodeConfig.getDestIp() != null && nodeConfig.getDestPort() != null) {
                runner.addArgument(nodeConfig.getDestIp());
                runner.addArgument(nodeConfig.getDestPort());
                info = "tcp/" + extPort;
            } else
                throw new BWFLAException("invalid node tcp config.");
            this.addControlConnector(new InfoDummyConnector(info));
        }

        runner.setLogger(LOG);
        if (!runner.start(false))
            throw new BWFLAException("Cannot start node process");
        vdeProcesses.add(runner);

        this.addControlConnector(new EthernetConnector(hwAddress, vdeSocketsPath, LOG));

        LOG.info("Initialized node-tcp instance");
    }

    @Override
    public String getComponentType() throws BWFLAException {
        return "nodetcp";
    }

    public static NodeTcpBean createNodeTcp(NodeTcpConfiguration config) throws ClassNotFoundException {
        String targetBean = "NodeTcpBean";

        Class<?> beanClass = Class.forName(NodeTcpBean.class.getPackage().getName() + "." + targetBean);
        return (NodeTcpBean) CDI.current().select(beanClass).get();
    }
}
