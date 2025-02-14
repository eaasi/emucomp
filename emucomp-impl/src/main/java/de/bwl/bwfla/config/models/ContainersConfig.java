package de.bwl.bwfla.config.models;

import io.quarkus.runtime.annotations.ConfigItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainersConfig {

    @ConfigItem
    public String blobstore;

    @ConfigItem
    public UserNamespaceConfig usernamespace;
}
