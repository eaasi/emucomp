package de.bwl.bwfla.emucomp.components.emulators;


import de.bwl.bwfla.emucomp.common.Drive;
import de.bwl.bwfla.emucomp.common.MachineConfiguration;
import de.bwl.bwfla.emucomp.common.Nic;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;

public class PreviousBean extends EmulatorBean {

    @Override
    protected void prepareEmulatorRunner() throws BWFLAException {
        emuRunner.setCommand("/usr/local/bin/Previous");
    }

    @Override
    protected String getEmulatorWorkdir()
    {
        return "/usr/local/bin/";
    }

    @Override
    protected String getEmuContainerName(MachineConfiguration env)
    {
        return "previous";
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
