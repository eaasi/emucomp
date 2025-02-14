package de.bwl.bwfla.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class ApplicationConfig {

    @Produces
    @ApplicationScoped
    public ComponentsConfig componentsConfig() {
        return new ComponentsConfig();
    }

    @Produces
    @ApplicationScoped
    public BlobConfig blobConfig() {
        return new BlobConfig();
    }
}
