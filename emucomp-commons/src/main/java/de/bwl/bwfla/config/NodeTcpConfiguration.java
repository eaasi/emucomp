package de.bwl.bwfla.config;


import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = "http://bwfla.bwl.de/common/datatypes")
@XmlRootElement(name = "nodetcp", namespace = "http://bwfla.bwl.de/common/datatypes")
public class NodeTcpConfiguration extends ComponentConfiguration {

    @XmlElement(required = true)
    private boolean dhcp;

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
}

