package de.bwl.bwfla.emucomp.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bwl.bwfla.emucomp.common.ComponentConfiguration;
import de.bwl.bwfla.emucomp.common.FileCollection;
import de.bwl.bwfla.emucomp.common.FileCollectionEntry;
import de.bwl.bwfla.emucomp.common.MachineConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class BindingsResolver {

    public static String providedConfigurationPath;
    private static StringBuffer extractedConfigurationHolder;
    private static ComponentConfiguration extractedComponentConfiguration;

    private static final ThreadLocal<ObjectMapper> mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    public static Optional<FileCollection> findFileCollectionDeclaration(String objectArchive, String id) {
        tryExtractConfiguration();

        if (extractedComponentConfiguration instanceof MachineConfiguration) {
            MachineConfiguration configuration = ((MachineConfiguration) extractedComponentConfiguration);

            if (configuration.getAbstractDataResource() != null) {
                Set<FileCollection> collect = configuration.getAttachedFiles()
                        .stream()
                        .filter(e -> e.getArchive().equals(id))
                        .filter(e -> e.getArchive().equals(objectArchive))
                        .collect(Collectors.toSet());

                return collect.stream().findFirst();
            }
        }
        return Optional.empty();
    }

    public static synchronized void tryExtractConfiguration() {
        if (extractedConfigurationHolder == null && (providedConfigurationPath == null || providedConfigurationPath.isEmpty())) {
            try {
                URL configDirUrl = BindingsResolver.class.getResource("/config");

                if (configDirUrl == null) {
                    throw new IOException("Configuration directory not found on classpath: /config");
                }

                URI configDirUri = configDirUrl.toURI();
                File configDir = new File(configDirUri);

                if (!configDir.exists() || !configDir.isDirectory()) {
                    throw new IOException("Resolved path is not a directory: " + configDir.getAbsolutePath());
                }

                File[] jsonFiles = configDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

                if (jsonFiles == null || jsonFiles.length == 0) {
                    throw new IOException("No JSON files found in directory: " + configDir.getAbsolutePath());
                }

                if (jsonFiles[0] != null) {
                    providedConfigurationPath = jsonFiles[0].getAbsolutePath();
                    String resourcePath = "/config" + jsonFiles[0].getName();
                    try (InputStream is = BindingsResolver.class.getResourceAsStream("/" + resourcePath)) {
                        if (is == null) {
                            throw new FileNotFoundException("Resource not found after selection: " + resourcePath);
                        }
                        String data = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        extractedConfigurationHolder = new StringBuffer(data);

                        extractedComponentConfiguration =  mapperThreadLocal.get().readValue(data, ComponentConfiguration.class);
                    }
                } else {
                    throw new IOException("No suitable JSON file could be chosen from: " + configDir.getAbsolutePath());
                }

            } catch (URISyntaxException | IOException e) {
                log.error("Failed to extract configuration from classpath directory: /config, " + e.getMessage());

            } catch (Exception e) {
                log.error("An error occurred during configuration processing: " + e.getMessage());
            }
        }
    }
}
