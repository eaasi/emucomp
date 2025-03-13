package de.bwl.bwfla.emucomp;


import com.google.gson.GsonBuilder;

import java.util.List;

public class FileCollection extends JsonType {
    public String id;

    public List<FileCollectionEntry> files;

    private String label;

    public FileCollection() {
        id = null;
    }

    public FileCollection(String id) {
        this.id = id;
    }

    public static FileCollection fromValue(String data) {
        return new GsonBuilder().create().fromJson(data, FileCollection.class);
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
