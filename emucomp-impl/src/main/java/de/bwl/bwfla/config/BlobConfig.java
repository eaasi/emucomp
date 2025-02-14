package de.bwl.bwfla.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@ConfigRoot(prefix = "blobstore")
public class BlobConfig {

    @ConfigItem(name = "gc_interval", defaultValue = "30min")
    private Duration gcInterval;
    @ConfigItem(name = "max_entry_age", defaultValue = "1hour")
    private Duration maxEntryAge;

    @ConfigItem
    private BlobBackendConfig backend;
}
