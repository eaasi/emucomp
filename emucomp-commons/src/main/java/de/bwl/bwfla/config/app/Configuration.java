package de.bwl.bwfla.config.app;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class Configuration {
    @ConfigItem
    public HelpersConfiguration helpers;

    @ConfigItem
    public RunnersConfiguration runners;

    @ConfigItem
    public CommonConfiguration commonconf;

    @ConfigItem
    public FileCacheConfig filecache;

    @ConfigItem
    public HandleConfiguration handle;
}
