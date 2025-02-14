package de.bwl.bwfla.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(prefix = "emucomp")
public class EmuCompConfig {

    @ConfigItem
    public String blobstoreSoap;

    @ConfigItem
    public String blobstoreRest;

    @ConfigItem
    public Long inactivitytimeout;

    @ConfigItem
    public Boolean enablePulseaudio;

    @ConfigItem
    public Boolean enableScreenshooter;

    @ConfigItem
    public Boolean debugBeanEnabled;

    @ConfigItem
    public String libfaketime;

    @ConfigItem
    public String curlProxy;

    @ConfigItem
    public String rompath;

    @ConfigItem
    public String alsaCard;

    @ConfigItem
    public String imageProxy;
}
