package de.bwl.bwfla.emucomp.components.emulators;

import de.bwl.bwfla.common.exceptions.BWFLAException;
import de.bwl.bwfla.common.utils.DeprecatedProcessRunner;
import de.bwl.bwfla.common.utils.NetworkUtils;
import de.bwl.bwfla.emucomp.Drive;
import de.bwl.bwfla.emucomp.MachineConfiguration;
import de.bwl.bwfla.emucomp.Nic;
import org.apache.tamaya.ConfigurationProvider;
import org.apache.tamaya.inject.api.Config;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static de.bwl.bwfla.emucomp.Drive.DriveType.*;

/**
 * @author iv1004
 *
 */
public class QemuBean extends EmulatorBean
{

	final boolean isGpuEnabled = ConfigurationProvider.getConfiguration().get("components.xpra.enable_gpu", Boolean.class);

	@Inject
	@Config("components.binary.qemu")
	protected String qemu_bin;

	private String monitor_path;

	@Override
	protected String getEmuContainerName(MachineConfiguration machineConfiguration)
	{
		return "qemu-system";
		/*
		if(machineConfiguration.getArch().equalsIgnoreCase("i386") || machineConfiguration.getArch().equalsIgnoreCase("x86_64"))
			return "qemu-system-x86";

		return "qemu-system-" + machineConfiguration.getArch();
		*/
	}

	public enum QEMU_ARCH {
		x86_64, ppc, i386
	}

	private boolean isValidQemuArch(String qemuArch) {
		for (QEMU_ARCH defined_arch : QEMU_ARCH.values()) {
			if (qemuArch.contains(defined_arch.name())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void prepareEmulatorRunner() throws BWFLAException
	{

		if(qemu_bin == null)
			throw new BWFLAException("EmulatorContainer's executable not found! Make sure you have specified " + "a valid path to your executable in the corresponding 'properties' file");

		if(!isValidQemuArch(qemu_bin))
			qemu_bin += emuEnvironment.getArch();

		File exec = new File(qemu_bin);
		//if (exec == null || !exec.exists())
		//	throw new BWFLAException("EmulatorContainer's executable not found! Make sure you have specified " + "a valid path to your executable in the corresponding 'properties' file");

		String config = this.getNativeConfig();
		// Initialize the process-runner
		if (isGpuEnabled) {
			LOG.warning("QEMU: GPU ENABLED!");
			emuRunner.setCommand("vglrun");
			emuRunner.addArgument(exec.getAbsolutePath() + "-gpu");
			emuRunner.addArguments("-L", "/usr/share/seabios/");
			emuRunner.addArguments("-L", "/usr/lib/ipxe/qemu/");
		} else
			emuRunner.setCommand(exec.getAbsolutePath());

		if(qemu_bin.contains("ppc"))
			emuRunner.addArguments("-L", "/usr/share/qemu");

		monitor_path = this.getSocketsDir().resolve("qemu-monitor-socket").toString();
		emuRunner.addArguments("-monitor", "unix:" + monitor_path + ",server,nowait");

		if (config != null && !config.isEmpty()) {
			String[] tokens = config.trim().split("\\s+");
			for (String token : tokens)
			{
				if(token.isEmpty())
					continue;

				if(token.contains("-enable-kvm"))
				{
					try{
						if(!kvmCheck())
							continue;

						super.isKvmDeviceEnabled = true;
					}
					catch(Exception e)
					{
						LOG.info(e.getMessage());
						continue;
					}
				}

				if(token.contains("nic,model="))
					token += ",macaddr=" + NetworkUtils.getRandomHWAddress();

				emuRunner.addArgument(token.trim());
			}
		}

		if (this.isLocalModeEnabled()) {
			emuRunner.addArgument("-full-screen");
		}
		else if (this.isSdlBackendEnabled()) {
			emuRunner.addArguments("-k", "en-us");
			if (this.isPulseAudioEnabled())
				emuRunner.addEnvVariable("QEMU_AUDIO_DRV", "pa");
			else emuRunner.addEnvVariable("QEMU_AUDIO_DRV", "sdl");
		} else if (this.isXpraBackendEnabled()){
			emuRunner.addEnvVariable("QEMU_AUDIO_DRV", "pa");
		}

		// Configure printer device
		final Path printerDataFile = this.getPrinterDir().resolve("printer.out");
		super.printer = new PostScriptPrinter(printerDataFile, this, LOG);
		emuRunner.addArguments("-chardev", "file,id=printer,path=" + printerDataFile.toString());
		emuRunner.addArguments("-parallel", "chardev:printer");
		emuContainerFilesToCheckpoint.add(printerDataFile.toString());
	}

	@Override
	public Set<String> getHotplugableDrives()
	{
		HashSet<String> set = new HashSet<String>();
		set.add(CDROM.name());
		set.add(DISK.name());
		set.add(FLOPPY.name());
		return set;
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
			LOG.warning("Drive doesn't reference a valid binding, attach canceled.");
			e.printStackTrace();
			return false;
		}

		switch (drive.getType()) {
			case FLOPPY:
				emuRunner.addArgument("-drive");
				emuRunner.addArgument("file=", imagePath.toString(), ",index=", drive.getUnit(), ",if=floppy");
				if (drive.isBoot())
					emuRunner.addArguments("-boot", "order=a");

				break;

			case DISK:
				// FIXME
				// we should come up with a better way of separating qemu binaries
				if (!qemu_bin.contains("ppc")) {
					emuRunner.addArgument("-drive");
					emuRunner.addArgument("file=", imagePath.toString(),
							",if=", drive.getIface(),
							",bus=", drive.getBus(),
							",unit=", drive.getUnit(),
							",media=disk");
				} else {
					emuRunner.addArgument("-drive");
					emuRunner.addArgument("file=", imagePath.toString());
				}
				if (drive.isBoot())
					emuRunner.addArguments("-boot", "order=c");

				break;

			case CDROM:

				if (!qemu_bin.contains("ppc")) {
					emuRunner.addArgument("-drive");
					emuRunner.addArgument("file=", imagePath.toString(), ",if=", drive.getIface(),
							",bus=", drive.getBus(),
							",unit=", drive.getUnit(),
							",media=cdrom");
				} else {
					emuRunner.addArguments("-cdrom", imagePath.toString());
				}

				if (drive.isBoot())
					emuRunner.addArguments("-boot", "order=d");

				break;

			default:
				LOG.severe("Device type '" + drive.getType() + "' not supported yet.");
				return false;
		}

		return true;
	}


	@Override
	public boolean connectDrive(Drive drive, boolean connect) {
		if (!isXpraBackendEnabled())
			if (!emuRunner.isProcessRunning()) {
				LOG.warning("Hotplug is unavailable because emulator is not running.");
				return false;
			}

		StringBuilder command = new StringBuilder();

		if (!connect) {
			// detach/eject
			command.append("eject -f ");

			switch (drive.getType()) {
				case FLOPPY:
					command.append("floppy" + drive.getUnit());
					break;
				case CDROM:
					command.append(drive.getIface().toLowerCase());
					command.append(drive.getBus());
					command.append("-");
					command.append("cd");
					command.append(drive.getUnit());
					command.append(" ");
					break;
				default:
					LOG.severe("Device type '" + drive.getType()
							+ "' is not hot-pluggable.");
					return false;
			}
		} else {
			if (drive == null || drive.getData() == null || drive.getData().isEmpty()) {
				this.sendMonitorCommand(command.toString());
				LOG.warning("Drive doesn't contain an image, empty drive.");
				return true;
			}

			Path imagePath = null;
			try {
				imagePath = Paths.get(this.lookupResource(drive.getData(), this.getImageFormatForDriveType(drive.getType())));
			} catch (Exception e) {
				LOG.warning("Drive doesn't reference a valid binding, attach cancelled.");
				return false;
			}

			command.append("change ");

			switch (drive.getType()) {
				case FLOPPY:
					command.append("floppy" + drive.getUnit());
					command.append(" ");
					break;
				case CDROM:
					command.append(drive.getIface().toLowerCase());
					command.append(drive.getBus());
					command.append("-");
					command.append("cd");
					command.append(drive.getUnit());
					command.append(" ");
					break;
				default:
					LOG.severe("Device type '" + drive.getType()
							+ "' is not hot-pluggable.");
					return false;
			}

			command.append(imagePath.toString());

		}
		this.sendMonitorCommand(command.toString());
		return true;
	}

//    @Override
//    protected VolatileDrive allocateDrive(DriveType type, Drive proto)
//    {
//        // Note: Qemu only supports 2 floppy drives (on same bus)
//        //       and 2 ide controllers (with 2 units each).
//
//        VolatileDrive result = new VolatileDrive();
//        result.setType(type);
//        result.setBoot(false);
//        result.setPlugged(true);
//// FIXME
////        result.setTransport(Resource.TransportType.FILE);
//
//        switch (type) {
//        case FLOPPY:
//            result.setIface("floppy");
//            // find first available floppy connector
//
//            // Logic: 2 - (0+1) = 1 => 0 taken, use 1
//            //        2 - (1+1) = 0 => 1 taken, use 0
//            //        2 - (0+1) - (1+1) = -1 => both taken, no drive available
//            int possibleUnit = 2;
//            for (Drive d : this.emuEnvironment.getDrive()) {
//                if (d.getType().equals(Drive.DriveType.FLOPPY)) {
//                    possibleUnit -= Integer.parseInt(d.getUnit()) + 1;
//                }
//            }
//            if (possibleUnit >= 2) {
//                // all connectors available, use first
//                possibleUnit = 0;
//            }
//            if (possibleUnit < 0) {
//                // no drive available
//                return null;
//            }
//
//            result.setBus("0");
//            result.setUnit(Integer.toString(possibleUnit));
//
//            return result;
//        case DISK:
//        case CDROM:
//            // HDDs and CD drives both go to the ide bus
//            result.setIface("ide");
//
//            // same logic as for floppy drives, only for two busses now
//            int possibleBus0 = 2;
//            int possibleBus1 = 2;
//            for (Drive d : this.emuEnvironment.getDrive())
//            {
//                if (d.getIface().equals("ide"))
//                {
//                    if (d.getBus().equals("0")) {
//                        possibleBus0 -= Integer.parseInt(d.getUnit()) + 1;
//                    }
//                    if (d.getBus().equals("1")) {
//                        possibleBus1 -= Integer.parseInt(d.getUnit()) + 1;
//                    }
//                }
//            }
//            if (possibleBus0 >= 2) {
//                possibleBus0 = 0;
//            }
//            if (possibleBus1 >= 2) {
//                possibleBus1 = 0;
//            }
//            if (possibleBus0 == 0 || possibleBus0 == 1) {
//                // connector on bus 0 available
//                result.setBus("0");
//                result.setUnit(Integer.toString(possibleBus0));
//            } else if (possibleBus1 == 0 || possibleBus1 == 1) {
//                // connector on bus 1 available
//                result.setBus("1");
//                result.setUnit(Integer.toString(possibleBus1));
//            } else {
//                // no ide drive available
//                return null;
//            }
//            
//            return result;
//        }
//        return null;
//    }

	protected boolean addNic(Nic nic) {
		if (nic == null) {
			LOG.warning("NIC is null, attach canceled.");
			return false;
		}

		emuRunner.addArgument("-net");
		emuRunner.addArgument("vde,sock=", this.getNetworksDir().resolve("nic_" + nic.getHwaddress()).toString());
		return true;
	}

	private void sendMonitorCommand(String command) {
		if(monitor_path == null){
			LOG.severe("qemu socket was not created!");
			return;
		}
			if (command != null && !command.isEmpty()) {
				if (this.isContainerModeEnabled()) {
					command = this.getContainerHostPathReplacer()
							.apply(command);
				}

				DeprecatedProcessRunner runner = new DeprecatedProcessRunner();
				runner.setCommand("/bin/bash");
				runner.addArgument("-c");
				runner.addArguments("echo " + command + " | socat - UNIX-CONNECT:" + monitor_path);
				runner.execute();
			} else {
				LOG.severe("Command to qemu monitor is not valid!");
			}
	}

	private boolean kvmCheck() throws IOException, BWFLAException
	{
		DeprecatedProcessRunner runner = new DeprecatedProcessRunner("kvm-ok");
		runner.redirectStdErrToStdOut(false);
		if(!runner.execute(false, false))
			throw new BWFLAException(runner.getStdErrString());

		boolean isKvmAvailable = false;

		if (runner.getStdOutString().contains("KVM acceleration can be used")) {
		isKvmAvailable = true;
		}

		runner.cleanup();
		return isKvmAvailable;
	}

	private String fmtDate(long epoch)
	{
		Date d = new Date(epoch);
		DateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss"); // 2006-06-17T16:01:21
		String formatted = format.format(d);
		return formatted;
	}

	protected void setEmulatorTime(long epoch)
	{
		LOG.info("set emulator time: "  + epoch + " " + "fmtStr" + fmtDate(epoch));
		emuRunner.addArgument("-rtc");
		emuRunner.addArgument("base="+fmtDate(epoch));
	}

}
