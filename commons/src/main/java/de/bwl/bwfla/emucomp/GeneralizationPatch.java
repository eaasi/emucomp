package de.bwl.bwfla.emucomp;

import com.google.gson.GsonBuilder;
import jakarta.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generalizationPatch", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
        "imageGeneralization",
})
@XmlRootElement(name="generalizationPatch", namespace = "http://bwfla.bwl.de/common/datatypes")
public class GeneralizationPatch extends Environment {

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    private ImageGeneralization imageGeneralization;

    public ImageGeneralization getImageGeneralization()
    {
        return imageGeneralization;
    }

    public static GeneralizationPatch fromValue(String value) {
        return new GsonBuilder().create().fromJson(value, GeneralizationPatch.class);
    }
}
