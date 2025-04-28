package de.bwl.bwfla.emucomp.common.utils;

import de.bwl.bwfla.emucomp.common.datatypes.QemuImage;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.common.services.net.HttpUtils;

import java.io.IOException;
import java.util.logging.Logger;


/**
 * Created by klaus on 29.08.17.
 */
public class ImageInformation {

    private String backingFile = null;
    private QemuImageFormat fileFormat = null;
    private QemuImage imageInfo;

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    static public String getBackingImageId(String bf) throws BWFLAException {
        if (bf.contains("exportname")) {
            return bf.substring(bf.lastIndexOf('=') + 1);
        }
        else if (HttpUtils.isAbsoluteUrl(bf)) {
            return bf.substring(bf.lastIndexOf('/') + 1);
        }
        else return bf;
    }

    public ImageInformation(String imageFile, Logger log) throws IOException, BWFLAException {
        ProcessRunner process = new ProcessRunner();
        process.setCommand("qemu-img");
        process.addArguments("info");
        process.addArguments("--output", "json");
        process.addArgument(imageFile);
        process.setLogger(log);

//        final ProcessRunner.Result result = process.executeWithResult()
//                .orElse(null);
//
//        if (result == null || !result.successful())
//            throw new BWFLAException("qemu-img info '" + imageFile + "' failed!");

        //COMMIT-ESCAPE
        var qemu = new QemuImage();
        qemu.setFilename("test");
        qemu.setFormat("qcow2");
        qemu.setBackingFile(imageFile);
        qemu.setVirtualSize("22");
        qemu.setFullBackingName("some-test");
        qemu.setBackingFileFormat("qcow2");

        imageInfo = qemu;
        process.cleanup();
    }

    public boolean hasBackingFile() {
        return imageInfo.getBackingFile() != null;
    }

    public String getBackingFile() {
        return imageInfo.getBackingFile();
    }

    public QemuImageFormat getFileFormat() {
        return QemuImageFormat.valueOf(imageInfo.getFormat().toUpperCase());
    }

    public QemuImageFormat getBackingFileFormat() {
        final var bfformat = imageInfo.getBackingFileFormat();
        return (bfformat != null) ? QemuImageFormat.valueOf(bfformat.toUpperCase()) : null;
    }

    public enum QemuImageFormat{
        // OPTIMIZATION: formats should be declared in frequency-descending order
        //               (e.g. most common first, followed by less common ones)
        QCOW2("qcow2"),
        RAW("raw"),
        VDI("vdi"),
        VHD("vpc"),
        VPC("vpc"),
        VMDK("vmdk"),
        EWF("ewf"),
        VHDX("vhdx");

        private final String format;

        private QemuImageFormat(String s) {
            this.format = s;
        }
        public String toString() {
            return this.format;
        }
    }
}
