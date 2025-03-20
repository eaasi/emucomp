package de.bwl.bwfla.emucomp;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generalizationPatch", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
        "imageGeneralization",
})
@XmlRootElement(name = "generalizationPatch", namespace = "http://bwfla.bwl.de/common/datatypes")
public class GeneralizationPatch extends Environment {

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    private ImageGeneralization imageGeneralization;

    public ImageGeneralization getImageGeneralization() {
        return imageGeneralization;
    }

    public static GeneralizationPatch fromValue(String value) {
        try {
            return objectMapperThreadLocal.get().readValue(value, GeneralizationPatch.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
