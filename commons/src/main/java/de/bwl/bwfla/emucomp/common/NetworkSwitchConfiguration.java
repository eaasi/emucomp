package de.bwl.bwfla.emucomp.common;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.xml.bind.annotation.XmlRootElement;
@JsonTypeName("networkSwitch")
@XmlRootElement(name="networkSwitch", namespace = "http://bwfla.bwl.de/common/datatypes")
public class NetworkSwitchConfiguration extends ComponentConfiguration {

}
