package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;

public class BinaryConfig {

    @ConfigItem
    public String qemu;

    @ConfigItem
    public String basilisk;

    @ConfigItem
    public String sheepshaver;

    @ConfigItem
    public String dosbox;

    @ConfigItem
    public String hatari;

    @ConfigItem
    public String kegs;

    @ConfigItem
    public String beebem;

    @ConfigItem
    public VboxConfig vbox;

    @ConfigItem
    public ViceConfig vice;

    @ConfigItem
    public PceConfig pce;

    @ConfigItem
    public String vdeswitch;

    @ConfigItem
    public String vdeslirp;

    @ConfigItem
    public String nodetcprunner;

    @ConfigItem
    public String nodetcpscript;

    @ConfigItem
    public String runc;

    @ConfigItem
    public String vdeplug;

    @ConfigItem
    public String vdesocks;
}
