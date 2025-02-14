package de.bwl.bwfla.config;


import de.bwl.bwfla.models.NetworkUtils;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = "http://bwfla.bwl.de/common/datatypes")
@XmlRootElement(name="vdeslirp", namespace = "http://bwfla.bwl.de/common/datatypes")
public class VdeSlirpConfiguration extends ComponentConfiguration {
    @XmlElement(required = false, nillable = false)
    private String hwAddress;

    @XmlElement(required = false, nillable = false, defaultValue = "10.0.2.2")
    private String ip4Address = "10.0.2.2";
    
    @XmlElement(required = false, nillable = false, defaultValue = "24")
    private Integer netmask = 24;
    
    @XmlElement(required = false, nillable = false, defaultValue = "true")
    private boolean dhcp = true;
    
    @XmlElement(required = false, nillable = false)
    private String dnsServer;

    public VdeSlirpConfiguration() {
        this.hwAddress = NetworkUtils.getRandomHWAddress();
    }
}
