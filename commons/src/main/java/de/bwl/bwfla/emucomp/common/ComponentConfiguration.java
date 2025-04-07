package de.bwl.bwfla.emucomp.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.bwl.bwfla.emucomp.common.utils.jaxb.JaxbType;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({VdeSlirpConfiguration.class,
        Environment.class,
        NetworkSwitchConfiguration.class,
        NodeTcpConfiguration.class})

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "configurationType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VdeSlirpConfiguration.class, name = "vdeSlirp"),
        @JsonSubTypes.Type(value = Environment.class, name = "environment"),
        @JsonSubTypes.Type(value = NetworkSwitchConfiguration.class, name = "networkSwitch"),
        @JsonSubTypes.Type(value = NodeTcpConfiguration.class, name = "nodeTcp"),
})
public abstract class ComponentConfiguration extends JaxbType {
}

