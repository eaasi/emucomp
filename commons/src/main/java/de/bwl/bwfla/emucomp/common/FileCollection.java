package de.bwl.bwfla.emucomp.common;

import de.bwl.bwfla.emucomp.common.utils.jaxb.JaxbType;
import lombok.Getter;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileCollection", namespace = "http://bwfla.bwl.de/common/datatypes")
@XmlRootElement(namespace = "http://bwfla.bwl.de/common/datatypes")
public class FileCollection extends JaxbType {
    @XmlElement(name = "file", namespace = "http://bwfla.bwl.de/common/datatypes")
    public List<FileCollectionEntry> files = new ArrayList<FileCollectionEntry>();

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    public String id;

    @XmlElement
    private String archive;

    @XmlElement
    private String label;

    public FileCollection() {
        id = null;
    }

    public FileCollection(String id) {
        this.id = id;
    }

    public FileCollection(String id, String archive, String label) {
        this.id = id;
        this.archive = archive;
        this.label = label;
    }

    public static FileCollection fromValue(String data) throws JAXBException {
        return JaxbType.fromValue(data, FileCollection.class);
    }

    public FileCollectionEntry getDefaultEntry() {
        for (FileCollectionEntry fc : files)
            if (fc.isDefault())
                return fc;

        return files.get(0);
    }

    public FileCollection copy() {
        try {
            return fromValue(this.value());
        } catch (JAXBException e) {
            // impossible
            return null;
        }
    }

    public void update() {
        for (final var file : files) {
            file.setArchive(this.archive);
            file.setObjectId(this.id);
        }
    }

    public FileCollectionEntry find(String resourceId) {
        for (final var resource : files) {
            if (!resourceId.equalsIgnoreCase(resource.getId()))
                continue;

            return resource;
        }

        return null;
    }

    public String resolve(String exportUrlPrefix, String resourceId) {
        final var resource = this.find(resourceId);
        return (resource != null) ? resource.resolve(exportUrlPrefix) : null;
    }

    public boolean contains(Binding.ResourceType rt) {
        for (final var resource : files) {
            if (resource.getResourceType() == rt)
                return true;
        }

        return false;
    }
}
