package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class TcpNodeConfig {

    @ConfigItem
    public String ports;
}
