package de.bwl.bwfla.emucomp;

import de.bwl.bwfla.common.utils.jaxb.JaxbType;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({VdeSlirpConfiguration.class,
             Environment.class,
             NetworkSwitchConfiguration.class,
             VdeSocksConfiguration.class,
             NodeTcpConfiguration.class})
public abstract class ComponentConfiguration extends JaxbType {

}
