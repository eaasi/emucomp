package de.bwl.bwfla.config.app;


import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@ConfigRoot(name = "http_export_servlet.file_cache")
public class FileCacheConfig {

    @ConfigItem
    private Duration gcInterval;

    @ConfigItem
    private Duration entryEvictionTimeout;
}
