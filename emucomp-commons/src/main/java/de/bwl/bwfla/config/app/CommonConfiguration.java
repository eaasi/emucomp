package de.bwl.bwfla.config.app;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(name = "commonconf")
public class CommonConfiguration {

        @ConfigItem
        public String serverdatadir;

        @ConfigItem
        public MongoConfiguration mongodb;
}
