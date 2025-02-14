package de.bwl.bwfla.config.app;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(name = "runners")
public class RunnersConfiguration {

    @ConfigItem
    public String tmpdirprefix;

    @ConfigItem
    public String stdoutfilename;

    @ConfigItem
    public String stderrfilename;
}
