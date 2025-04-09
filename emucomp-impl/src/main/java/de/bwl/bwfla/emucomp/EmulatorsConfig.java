package de.bwl.bwfla.emucomp;

import org.eclipse.microprofile.config.inject.ConfigProperties;

import javax.inject.Singleton;

@Singleton
@ConfigProperties(prefix = "emulators")
public class EmulatorsConfig {

    public String qemu;
    public String basilisk;
    public String sheepshaver;
    public String dosbox;
    public VBoxConfig vbox;
    public String hatari;
    public String vdeSwitch;
    public ViceConfig vice;
    public PceConfig pce;
    public String kegs;
    public String beebem;

    public static class VBoxConfig {
        public String headlessExec;
        public String manageExec;
    }

    public static class ViceConfig {
        public String c64;
        public String c128;
    }

    public static class PceConfig {
        public String atarist;
        public String ibmpc;
        public String macplus;
    }
}
