package de.bwl.bwfla.config.app;


import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(prefix = "handle")
public class HandleConfiguration {

    @ConfigItem
    private String prefix;

    @ConfigItem
    private AuthenticationConfiguration authentication;
}

