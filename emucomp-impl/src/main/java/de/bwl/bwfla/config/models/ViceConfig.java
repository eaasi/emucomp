package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class ViceConfig {

    @ConfigItem
    public String c64;

    @ConfigItem
    public String c128;
}
