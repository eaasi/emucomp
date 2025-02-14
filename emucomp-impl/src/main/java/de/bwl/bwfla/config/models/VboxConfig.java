package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class VboxConfig {

    @ConfigItem
    public String headlessexec;

    @ConfigItem
    public String manageexec;
}
