package de.bwl.bwfla.emucomp;

import org.eclipse.microprofile.config.ConfigProvider;

import java.util.logging.Logger;

public class XmountOptions {
    private boolean readonly = false;
    private EmulatorUtils.XmountOutputFormat outFmt;
    private EmulatorUtils.XmountInputFormat inFmt;
    private long offset = 0;
    private long size = -1;
    private static String curlProxySo = ConfigProvider.getConfig().getValue("emucomp.curl_proxy", String.class);

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    public XmountOptions() {
        this(EmulatorUtils.XmountOutputFormat.RAW);
    }

    public XmountOptions(EmulatorUtils.XmountOutputFormat outFmt) {
        this.outFmt = outFmt;
        inFmt = EmulatorUtils.XmountInputFormat.QEMU;
    }

    public void setOffset(long off) {
        this.offset = off;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setInFmt(EmulatorUtils.XmountInputFormat inFmt) {
        this.inFmt = inFmt;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public EmulatorUtils.XmountInputFormat getInFmt() {
        return inFmt;
    }

    public EmulatorUtils.XmountOutputFormat getOutFmt() {
        return outFmt;
    }

    public void setXmountOptions(DeprecatedProcessRunner process) {
        if (offset > 0 && size < 0) {
            process.addArguments("--offset", "" + offset);
        }

        process.addArguments("--out", outFmt.toString());
        if (!readonly) {
            process.addArguments("--cache", "writethrough");
            process.addArguments("--inopts", "qemuwritable=true,bdrv_cache=writeback");
        }

        if (size >= 0) {
            process.addArguments("--morph", "trim");
            String morphOpts;
            if (offset == 0)
                morphOpts = "size=" + size;
            else
                morphOpts = "offset=" + offset + ",size=" + size;
            process.addArguments("--morphopts", morphOpts);
        }

        String proxyUrl = MachineTokenProvider.getAuthenticationProxy();
        if (proxyUrl == null)
            proxyUrl = MachineTokenProvider.getProxy();

        log.warning("using http_proxy " + proxyUrl);
        process.addEnvVariable("LD_PRELOAD", curlProxySo);
        process.addEnvVariable("prefix_proxy", proxyUrl);

    }
}
