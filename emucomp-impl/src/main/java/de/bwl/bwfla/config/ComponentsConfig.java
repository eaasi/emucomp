package de.bwl.bwfla.config;

import de.bwl.bwfla.config.models.*;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@ConfigRoot(prefix = "components")
public class ComponentsConfig {

    @ConfigItem
    public Duration clientTimeout;

    @ConfigItem
    public Duration warmupTimeout;

    @ConfigItem
    public Duration timeout;

    @ConfigItem
    public BinaryConfig binary;

    @ConfigItem
    public SocksConfig socks;

    @ConfigItem
    public XpraConfig xpra;

    @ConfigItem
    public String viceDefaultsDir;

    @ConfigItem
    public EmulatorContainersConfig emulatorContainers;

    @ConfigItem
    public ContainersConfig containers;

    @ConfigItem
    public SessionStatisticsConfig sessionStatistics;

    @ConfigItem
    public TcpNodeConfig tcpNode;
}
