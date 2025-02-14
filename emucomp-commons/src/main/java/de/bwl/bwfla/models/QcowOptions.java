package de.bwl.bwfla.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QcowOptions {
    private String backingFile;
    private String size;
    private String proxyUrl;
}
