package de.bwl.bwfla.emucomp;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VdeSlirpConfiguration.class, name = "vdeSlirp"),
        @JsonSubTypes.Type(value = Environment.class, name = "environment"),
        @JsonSubTypes.Type(value = NetworkSwitchConfiguration.class, name = "networkSwitch"),
        @JsonSubTypes.Type(value = VdeSocksConfiguration.class, name = "vdeSocks"),
        @JsonSubTypes.Type(value = NodeTcpConfiguration.class, name = "nodeTcp")
})
public abstract class ComponentConfiguration extends JsonType{

}
