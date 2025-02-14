package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNamespaceConfig {

    @ConfigItem
    public boolean enabled;

    @ConfigItem
    public String user;

    @ConfigItem
    public String group;
}
