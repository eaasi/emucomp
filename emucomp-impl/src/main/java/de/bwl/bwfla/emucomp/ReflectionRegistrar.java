package de.bwl.bwfla.emucomp;

import de.bwl.bwfla.emucomp.common.Environment;
import de.bwl.bwfla.emucomp.common.NetworkSwitchConfiguration;
import de.bwl.bwfla.emucomp.common.NodeTcpConfiguration;
import de.bwl.bwfla.emucomp.common.VdeSlirpConfiguration;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
        VdeSlirpConfiguration.class,
        Environment.class,
        NetworkSwitchConfiguration.class,
        NodeTcpConfiguration.class
})
public class ReflectionRegistrar {
}
