package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class PceConfig {

    @ConfigItem
    public String atarist;

    @ConfigItem
    public String ibmpc;

    @ConfigItem
    public String macplus;
}
