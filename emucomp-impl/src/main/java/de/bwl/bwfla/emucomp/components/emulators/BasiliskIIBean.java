package de.bwl.bwfla.emucomp.components.emulators;
import de.bwl.bwfla.emucomp.common.Drive;
import de.bwl.bwfla.emucomp.common.MachineConfiguration;
import de.bwl.bwfla.emucomp.common.Nic;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * 
 * @author Johann Latocha <johann.latocha@rz.uni-freiburg.de>
 * 
 */
public class BasiliskIIBean extends EmulatorBean
{
    @Inject
    @ConfigProperty(name = "components.binary.basilisk")
    private String basiliskBean;

	@Override
	protected String getEmuContainerName(MachineConfiguration env)
	{
		return "basiliskII";
	}

	@Override
	public void prepareEmulatorRunner()
	{
		emuConfig.setHardTermination(true);
		emuRunner.setCommand(basiliskBean);
        HashMap<String, String> conf = prepareConfig(this.getNativeConfig());
        for(String key : conf.keySet())
        {
            emuRunner.addArgument("--" + key.trim());
            if (conf.get(key) != null)
                emuRunner.addArgument(conf.get(key).trim());

        }
	}

	@Override
	public boolean addDrive(Drive drive) {
	    if (drive == null || (drive.getData() == null)) {
            LOG.warning("Drive doesn't contain an image, attach canceled.");
            return false;
	    }
	    
	    Path imagePath = null;
	    try {
	        imagePath = Paths.get(this.lookupResource(drive.getData(), this.getImageFormatForDriveType(drive.getType())));
	    } catch (Exception e) {
            LOG.warning("Drive doesn't reference a valid binding, attach cancelled.");
            LOG.log(Level.WARNING, e.getMessage(), e);
            return false;
	    }
	    
        switch (drive.getType()) {
        case FLOPPY:
            try {
                final Path link = this.getDataDir()
						.resolve("floppy-" + drive.getBus() + "-" + drive.getUnit() + ".img");

                Files.deleteIfExists(link);
                Files.createSymbolicLink(link, imagePath);
                imagePath = link;
                LOG.info("adding floppy ");
            } catch (IOException e) {
                LOG.warning("Cannot create generic symlink for floppy image, attach cancelled.");
                return false;
            }
            emuRunner.addArgument("--disk");
            emuRunner.addArgument(imagePath.toString());
            break;

        case DISK:
            emuRunner.addArgument("--disk");
            emuRunner.addArgument(imagePath.toString());
            break;

        case CDROM:
            try {
                final Path link = this.getDataDir()
						.resolve("cdrom-" + drive.getBus() + "-" + drive.getUnit() + ".iso");

                Files.deleteIfExists(link);
                Files.createSymbolicLink(link, imagePath);
                imagePath = link;
            } catch (IOException e) {
                LOG.warning("Cannot create generic symlink for cdrom image, attach cancelled.");
                return false;
            }
            emuRunner.addArgument("--cdrom");
            emuRunner.addArgument(imagePath.toString());
            break;

        default:
            LOG.severe("Device type '" + drive.getType() + "' not supported yet.");
            return false;
        }

        return true;
	}
    
    @Override
    public int changeMedium(int containerId, String objReference) throws BWFLAException {
        throw this.newNotSupportedException();
    }
    
    @Override
    public boolean connectDrive(Drive drive, boolean attach) throws BWFLAException {
        throw this.newNotSupportedException();

        // This code WOULD implement hotswapping media IF BasiliskII would allow
        // it
        /*
        if (drive == null) {
            LOG.warning("Drive is null, (de-)attach cancelled.");
            return false;
        }

        Path imagePath = Paths.get(this.lookupResource(drive.getData()));
        if (attach) {
            if (imagePath == null || !Files.exists(imagePath)) {
                LOG.warning("Drive doesn't reference a valid binding, attach cancelled.");
                return false;
            }
        } else {
            imagePath = Paths.get("/dev/null");
        }

        switch (drive.getType()) {
        case FLOPPY:
            try {
                Path link = this.tempDir.toPath().resolve(
                        Paths.get("floppy-" + drive.getBus() + "-"
                                + drive.getUnit() + ".img"));
                Files.deleteIfExists(link);
                Files.createSymbolicLink(link, imagePath);
            } catch (IOException e) {
                LOG.severe("Could not remove symbolic link to floppy. Detach cancelled.");
                return false;
            }
            break;

        case DISK:
            LOG.warning("Hotplug for disk drives is not supported by this emulator.");
            return false;

        case CDROM:
            try {
                Path link = this.tempDir.toPath().resolve(
                        Paths.get("cdrom-" + drive.getBus() + "-"
                                + drive.getUnit() + ".iso"));
                Files.deleteIfExists(link);
                Files.createSymbolicLink(link, imagePath);
            } catch (IOException e) {
                LOG.severe("Could not remove symbolic link to cdrom. Detach cancelled.");
                return false;
            }
            break;

        default:
            LOG.severe("Device type '" + drive.getType()
                    + "' not supported yet.");
            return false;
        }
        return true;
        */
    }

	@Override
	protected boolean addNic(Nic nic) {

	    emuRunner.addArguments("--switch", this.getNetworksDir().resolve("nic_" + nic.getHwaddress()).toString());
		return true;
	}

    private HashMap<String, String> prepareConfig(String config)
    {
        HashMap<String,String> confValues = loadDefaults();
        this.parseConfig(config, confValues);

        if (this.getEmuBeanMode() == EmulatorBeanMode.XPRA) {
            String screenConf = confValues.get("screen");
            if (screenConf != null)
                screenConf = screenConf.replace("dga", "win");
            confValues.put("screen", screenConf);
        }

        return confValues;
    }

	private void parseConfig(String config, HashMap<String, String> confValues)
    {
        if (config == null || config.isBlank())
            return;

        String[] tokens = config.trim().split("\n");
        for (String token : tokens) {
            String[] args = token.trim().split("\\s+");
            if (args.length != 2) {
                LOG.warning("Native config is malformed! Skipping '" + token + "'!");
                continue;
            }

            if(args[0].equals("ether") && args[1].equals("slirp"))
				args[1] = "vde";

            confValues.put(args[0], args[1]);
        }
    }

    private HashMap<String, String> loadDefaults()
    {
        HashMap<String, String> defValues = new HashMap<String,String>();
        final String defaultsName = "basiliskii.defaults";
        ClassLoader cloader = this.getClass().getClassLoader();

        try (final InputStream instream = cloader.getResourceAsStream(defaultsName)) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            String line = reader.readLine();
            while (line != null)
            {
                String[] args = line.trim().split("\\s+");
                if (args.length < 1 || args.length > 2) {
                    LOG.warning("check your defaults file, some 'param-value' pairs are malformed");
                    continue;
                }

                if(args.length == 2)
                    defValues.put(args[0], args[1]);
                else
                    defValues.put(args[0], null);
                line = reader.readLine();
            }

        } catch (IOException e) {
            LOG.info(e.getMessage());
        }

        return defValues;
    }
}
