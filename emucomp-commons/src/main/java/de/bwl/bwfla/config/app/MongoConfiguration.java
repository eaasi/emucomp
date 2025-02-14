package de.bwl.bwfla.config.app;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(name = "commonconf.mongodb")
public class MongoConfiguration {

    @ConfigItem(defaultValue = "mongodb://localhost:27017")
    private String address;

    @ConfigItem(defaultValue = "eaas")
    private String database;
}
