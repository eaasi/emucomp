package de.bwl.bwfla.config.app;


import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(prefix = "authentication")
public class AuthenticationConfiguration {

    @ConfigItem(defaultValue = "/home/bwfla/server-data/hdl-keys/admpriv.bin")
    private String privateKeyFile;

    @ConfigItem(defaultValue = "0.NA/11270")
    private String handle;

    @ConfigItem(defaultValue = "300")
    private Integer index;
}

