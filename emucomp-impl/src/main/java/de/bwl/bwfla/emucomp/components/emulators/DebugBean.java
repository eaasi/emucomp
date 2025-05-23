package de.bwl.bwfla.emucomp.components.emulators;


import de.bwl.bwfla.emucomp.common.Drive;
import de.bwl.bwfla.emucomp.common.Nic;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;


public class DebugBean extends EmulatorBean {
    @Inject
    @ConfigProperty(name = "emucomp.debug_bean_enabled")
    public boolean isEnabled;

    @Override
    protected void prepareEmulatorRunner() throws BWFLAException {
        if (isEnabled) {
            String nativeConfig = this.getNativeConfig();
            emuRunner.setCommand(nativeConfig);
        }
    }

    @Override
    protected boolean addDrive(Drive drive) {
        return false;
    }

    @Override
    protected boolean connectDrive(Drive drive, boolean attach) {
        return false;
    }

    @Override
    protected boolean addNic(Nic nic) {
        return false;
    }
}
