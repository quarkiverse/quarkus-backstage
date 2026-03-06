package io.quarkiverse.backstage.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

public final class DevServices {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevServices.class.getName());
    private static final Path CURRENT_DIR = Path.of(System.getProperty("user.dir"));
    private static final Path PROJECT_ROOT_DIR = Projects.getProjectRoot(CURRENT_DIR);

    /**
     * Checks if the dev service is available
     *
     * @param name the name of the dev service
     * @return true if the dev service is available
     */
    public static boolean hasDevServicesConfiguration(String name) {
        return findRunningDevServicesConfiguration(name).length > 0;
    }

    /**
     * Finds the running dev services configuration
     *
     * @param name the name of the dev service
     * @return an array of files
     */
    public static File[] findRunningDevServicesConfiguration(String name) {
        Path devServiceDir = PROJECT_ROOT_DIR.resolve(".quarkus").resolve("dev").resolve(name);
        if (!devServiceDir.toFile().exists()) {
            return new File[0];
        }
        return devServiceDir.toFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return isValidDevServiceFile(f);
            }
        });
    }

    /**
     * Gets the dev services configuration for the given dev service.
     * If multiple configurations are found, the most recent one is returned.
     *
     * @param name the name of the dev service
     * @return a map of configuration properties
     */
    public static Map<String, String> getDevServicesConfiguration(String name) {
        File[] files = findRunningDevServicesConfiguration(name);
        return Arrays.stream(files)
                .filter(File::exists)
                .sorted((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()))
                .findFirst()
                .map(f -> Serialization.unmarshal(f, new TypeReference<Map<String, String>>() {
                }))
                .orElse(new HashMap<String, String>());
    }

    /**
     * Finds all dev services configurations
     *
     * @param name the name of the dev service
     * @return an array of files
     */
    public static File[] findAllDevServicesConfiguration(String name) {
        Path devServiceDir = PROJECT_ROOT_DIR.resolve(".quarkus").resolve("dev").resolve(name);
        return devServiceDir.toFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {

                return !f.isDirectory() && f.getName().endsWith(".yaml");
            }
        });
    }

    /**
     * Removes all stale dev services configurations
     *
     * @param name the name of the dev service
     */
    public static void cleanupDevServicesConfiguration(String name) {
        File[] files = findAllDevServicesConfiguration(name);
        for (File f : files) {
            if (!isValidDevServiceFile(f)) {
                if (!f.delete()) {
                    LOGGER.warn("Failed to delete file: " + f.getName());
                }
            }
        }
    }

    private static boolean isValidDevServiceFile(File f) {
        if (f.isDirectory() || !f.getName().endsWith(".yaml")) {
            return false;
        }
        String name = f.getName().replace(".yaml", "");
        try {
            return name.matches("\\d+") && ProcessHandle.of(Long.parseLong(name)).map(ProcessHandle::isAlive).orElse(false);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
