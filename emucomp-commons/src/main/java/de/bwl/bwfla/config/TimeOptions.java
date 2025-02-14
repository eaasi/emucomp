package de.bwl.bwfla.config;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timeOptions", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
        "offset",
        "epoch",
})
public class TimeOptions {

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    private String offset;

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    private String epoch;
}
