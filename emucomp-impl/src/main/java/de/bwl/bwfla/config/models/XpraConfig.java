package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class XpraConfig {

    @ConfigItem
    public String ports;

    @ConfigItem
    public boolean enableGpu;
}
