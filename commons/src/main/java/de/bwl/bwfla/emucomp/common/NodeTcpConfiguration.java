package de.bwl.bwfla.emucomp.common;



import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = "http://bwfla.bwl.de/common/datatypes")
@XmlRootElement(name = "nodetcp", namespace = "http://bwfla.bwl.de/common/datatypes")
public class NodeTcpConfiguration extends ComponentConfiguration {

    @XmlElement(required = true)
    private boolean dhcp;

    @XmlElement
    private String dhcpNetworkAddress;

    @XmlElement
    private String dhcpNetworkMask;

    @XmlElement
    private String destIp;

    @XmlElement
    private String destPort;

    @XmlElement(required = true, nillable = false)
    private String hwAddress;

    @XmlElement
    private String socksUser;

    @XmlElement
    private String socksPasswd;

    @XmlElement(required = true)
    private boolean socksMode;

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public String getDestPort() {
        return destPort;
    }

    public void setDestPort(String destPort) {
        this.destPort = destPort;
    }

    public String getHwAddress() {
        return hwAddress;
    }

    public void setHwAddress(String hwAddress) {
        this.hwAddress = hwAddress;
    }

    public boolean isSocksMode() {
        return socksMode;
    }

    public void setSocksMode(boolean socksMode) {
        this.socksMode = socksMode;
    }

    public String getSocksUser() {
        return socksUser;
    }

    public void setSocksUser(String socksUser) {
        this.socksUser = socksUser;
    }

    public String getSocksPasswd() {
        return socksPasswd;
    }

    public void setSocksPasswd(String socksPasswd) {
        this.socksPasswd = socksPasswd;
    }

    public boolean isDhcp() {
        return dhcp;
    }

    public void setDhcp(boolean dhcp) {
        this.dhcp = dhcp;
    }

    public String getDhcpNetworkAddress() {
        return dhcpNetworkAddress;
    }

    public void setDhcpNetworkAddress(String dhcpNetworkAddress) {
        this.dhcpNetworkAddress = dhcpNetworkAddress;
    }

    public String getDhcpNetworkMask() {
        return dhcpNetworkMask;
    }

    public void setDhcpNetworkMask(String dhcpNetworkMask) {
        this.dhcpNetworkMask = dhcpNetworkMask;
    }
}

