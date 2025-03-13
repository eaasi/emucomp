package de.bwl.bwfla.emucomp;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.xml.bind.annotation.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "dataResourceType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ObjectArchiveBinding.class, name = "ObjectArchiveBinding"),
        @JsonSubTypes.Type(value = ImageArchiveBinding.class, name = "ImageArchiveBinding"),
        @JsonSubTypes.Type(value = BlobStoreBinding.class, name = "BlobStoreBinding"),
        @JsonSubTypes.Type(value = Binding.class, name = "Binding")
})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractDataResource", namespace = " http://bwfla.bwl.de/common/datatypes")
@XmlSeeAlso({
        ObjectArchiveBinding.class,
        ImageArchiveBinding.class,
        BlobStoreBinding.class,
        Binding.class
})
@XmlRootElement(namespace = "http://bwfla.bwl.de/common/datatypes")
public abstract class AbstractDataResource extends JsonType {
    @XmlAttribute(name = "id")
    protected String id;

    @XmlAttribute(name = "dataResourceType")
    protected String dataResourceType;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }
}
