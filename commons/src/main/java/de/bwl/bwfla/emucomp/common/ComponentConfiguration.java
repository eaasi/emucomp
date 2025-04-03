package de.bwl.bwfla.emucomp.common;

import de.bwl.bwfla.emucomp.common.utils.jaxb.JaxbType;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({VdeSlirpConfiguration.class,
             Environment.class,
             NetworkSwitchConfiguration.class,
             NodeTcpConfiguration.class})
public abstract class ComponentConfiguration extends JaxbType {

}
