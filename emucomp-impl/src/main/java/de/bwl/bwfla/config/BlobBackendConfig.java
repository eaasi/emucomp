package de.bwl.bwfla.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(prefix = "backend")
public class BlobBackendConfig {

    @ConfigItem(name = "type", defaultValue = "fs")
    private String type;
    @ConfigItem(name = "base_dir", defaultValue = "/tmp/blobstore")
    private String base_dir;
}
