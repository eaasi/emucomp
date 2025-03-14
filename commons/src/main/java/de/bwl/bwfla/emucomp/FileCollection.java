package de.bwl.bwfla.emucomp;


import com.fasterxml.jackson.core.JsonProcessingException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class FileCollection extends JsonType {

    public String id;

    public List<FileCollectionEntry> files;

    @XmlElement
    private String label;

    public FileCollection() {
        id = null;
    }

    public FileCollection(String id) {
        this.id = id;
    }

    public static FileCollection fromValue(String data) {
        try {
            return objectMapperThreadLocal.get().readValue(data, FileCollection.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public FileCollectionEntry getDefaultEntry() {
        for (FileCollectionEntry fc : files)
            if (fc.isDefault())
                return fc;

        return files.get(0);
    }

    public FileCollection copy() {
        return fromValue(this.value());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
