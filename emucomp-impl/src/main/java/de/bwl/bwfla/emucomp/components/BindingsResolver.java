package de.bwl.bwfla.emucomp.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bwl.bwfla.emucomp.common.ComponentConfiguration;
import de.bwl.bwfla.emucomp.common.FileCollection;
import de.bwl.bwfla.emucomp.common.ImageArchiveBinding;
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

    private static final String _classpath_DEFAULT_CONFIGURATION_PATH = "/config";

    public static volatile String providedConfigurationPath;
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
                        .filter(e -> e.getId().equals(id))
                        .filter(e -> e.getArchive().equals(objectArchive))
                        .collect(Collectors.toSet());

                return collect.stream().findFirst();
            }
        }
        return Optional.empty();
    }

    public static Optional<ImageArchiveBinding> findFirstImageArchiveBindingDeclaration() {
        tryExtractConfiguration();

        if (extractedComponentConfiguration instanceof MachineConfiguration) {
            MachineConfiguration configuration = ((MachineConfiguration) extractedComponentConfiguration);

            if (configuration.getAbstractDataResource() != null) {
                Set<ImageArchiveBinding> collect = configuration.getAbstractDataResource()
                        .stream()
                        .filter(e -> e instanceof ImageArchiveBinding)
                        .map(e -> (ImageArchiveBinding) e)
                        .collect(Collectors.toSet());
                if (collect.isEmpty()) {
                    log.info("Cannot find any ImageArchiveBinding configuration at {}", providedConfigurationPath);
                    return Optional.empty();
                }

                log.info("Found {} ImageArchiveBinding declarations, returning {}", collect.size(), collect.stream().findFirst().get().getImageId());
                return collect.stream().findFirst();
            }
        }
        return Optional.empty();
    }

    public static synchronized void tryExtractConfiguration() {
        if (extractedConfigurationHolder == null && (providedConfigurationPath == null || providedConfigurationPath.isEmpty())) {
            try {
                URL configDirUrl = BindingsResolver.class.getResource(_classpath_DEFAULT_CONFIGURATION_PATH);

                if (configDirUrl == null) {
                    throw new IOException("Configuration directory not found on classpath: " + _classpath_DEFAULT_CONFIGURATION_PATH);
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
                    loadConfiguration(_classpath_DEFAULT_CONFIGURATION_PATH + jsonFiles[0].getName());
                } else {
                    throw new IOException("No suitable JSON file could be chosen from: " + configDir.getAbsolutePath());
                }
                return;
            } catch (URISyntaxException | IOException e) {
                log.error("Failed to extract configuration from classpath directory: {}, " + e.getMessage(), _classpath_DEFAULT_CONFIGURATION_PATH);
            } catch (Exception e) {
                log.error("An error occurred during configuration processing: " + e.getMessage());
            }
        }

        File file = new File(providedConfigurationPath);
        if (file.isDirectory()) {
            try {
                File[] jsonFiles = file.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

                if (jsonFiles == null || jsonFiles.length == 0) {
                    throw new IOException("No JSON files found in directory: " + file.getAbsolutePath());
                }
                loadConfiguration(jsonFiles[0].getAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to extract configuration from classpath directory: {}, " + e.getMessage(), file.getAbsolutePath());
            }
        } else {
            loadConfiguration(providedConfigurationPath);
        }
    }

    private static void loadConfiguration(String path) {
        try (InputStream is = BindingsResolver.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found after selection: " + path);
            }
            String data = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            extractedConfigurationHolder = new StringBuffer(data);

            extractedComponentConfiguration = mapperThreadLocal.get().readValue(data, ComponentConfiguration.class);
        } catch (IOException e) {
            log.error("Cannot load configuration from {}", path);
        }
    }
}
