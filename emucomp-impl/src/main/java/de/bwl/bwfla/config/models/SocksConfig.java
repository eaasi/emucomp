package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class SocksConfig {

    @ConfigItem
    public String ports;

    @ConfigItem
    public String listenaddress;
}
