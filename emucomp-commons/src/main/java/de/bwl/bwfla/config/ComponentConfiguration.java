package de.bwl.bwfla.config;


import de.bwl.bwfla.jaxb.JaxbType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({VdeSlirpConfiguration.class,
        Environment.class,
        NetworkSwitchConfiguration.class,
        VdeSocksConfiguration.class,
        NodeTcpConfiguration.class})
public abstract class ComponentConfiguration extends JaxbType {

}
