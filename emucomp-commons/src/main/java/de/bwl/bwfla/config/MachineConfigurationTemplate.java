package de.bwl.bwfla.config;


import de.bwl.bwfla.jaxb.JaxbType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "emulationEnvironmentTemplate", namespace = "http://bwfla.bwl.de/common/datatypes")
public class MachineConfigurationTemplate extends MachineConfiguration {
    public static MachineConfigurationTemplate fromValue(String data) throws JAXBException {
        return JaxbType.fromValue(data, MachineConfigurationTemplate.class);
    }

    @Override
    public MachineConfigurationTemplate copy() {
        try {
            return MachineConfigurationTemplate.fromValue(this.value());
        } catch (JAXBException e) {
            Logger.getLogger(MachineConfigurationTemplate.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    public MachineConfiguration implement() throws JAXBException {

        MachineConfiguration m = new MachineConfiguration(this);

        JAXBContext jc = JAXBContext.newInstance(MachineConfiguration.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter w = new StringWriter();
        marshaller.marshal(m, w);
        return MachineConfiguration.fromValue(w.toString());
    }
}
