package de.bwl.bwfla.emucomp.components.emulators;

import de.bwl.bwfla.emucomp.common.Drive;
import de.bwl.bwfla.emucomp.common.MachineConfiguration;
import de.bwl.bwfla.emucomp.common.Nic;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class VMacMiniBean extends EmulatorBean {
    @Override
    protected void prepareEmulatorRunner() throws BWFLAException {
        emuRunner.setCommand("/minivmac/minivmac");
        emuRunner.addEnvVariable("LD_LIBRARY_PATH", "/usr/local/lib");
        emuRunner.setWorkingDirectory(Paths.get("/minivmac"));

        String config = this.getNativeConfig();
        if (config != null && !config.isEmpty()) {
            String[] tokens = config.trim().split("\\s+");
            for (String token : tokens)
            {
                if(token.isEmpty())
                    continue;

                emuRunner.addArgument(token.trim());
            }
        }
    }

    @Override
    protected String getEmulatorWorkdir()
    {
        return "/minivmac";
    }

    @Override
    protected String getEmuContainerName(MachineConfiguration env)
    {
        return "vmacmini";
    }

    @Override
    protected boolean addDrive(Drive drive) {
        if (drive == null || (drive.getData() == null)) {
            LOG.severe("Drive doesn't contain an image, attach canceled.");
            return false;
        }
        Path imagePath = null;
        try {
            imagePath = Paths.get(this.lookupResource(drive.getData(), this.getImageFormatForDriveType(drive.getType())));

            emuRunner.addArguments(imagePath.toString());
            return true;
        } catch (Exception e) {
            LOG.warning("Drive doesn't reference a valid binding, attach canceled." + e.getMessage());
            LOG.log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected boolean connectDrive(Drive drive, boolean attach) throws BWFLAException {
        throw this.newNotSupportedException();
    }

    @Override
    protected boolean addNic(Nic nic) throws BWFLAException {
        throw this.newNotSupportedException();
    }
}
