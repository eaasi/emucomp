package de.bwl.bwfla.emucomp.client;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "internal")
public interface InternalConfig {

    ObjectArchive objectarchive();

    interface ObjectArchive {
        String url();
    }
}