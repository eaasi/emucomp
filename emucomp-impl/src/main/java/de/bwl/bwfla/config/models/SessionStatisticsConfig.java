package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class SessionStatisticsConfig {

    @ConfigItem
    public String flushDelay;
}
