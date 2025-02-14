package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class EmulatorContainersConfig {

    @ConfigItem
    public boolean enabled;

    @ConfigItem
    public boolean snapshot;

    @ConfigItem
    public int uid;

    @ConfigItem
    public int gid;
}
