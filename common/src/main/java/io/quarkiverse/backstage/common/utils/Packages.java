package io.quarkiverse.backstage.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

public final class Packages {

    private static final Logger LOG = Logger.getLogger(Packages.class);

    public static Optional<String> findCommonPackagePrefix(Path projectDir) {
        List<String> packages = new ArrayList<>();

        // Find all module directories with "src/main/java" or "src/test/java"
        try {
            Files.walk(projectDir, 1)
                    .filter(Files::isDirectory)
                    .forEach(moduleDir -> {
                        Path mainSrcDir = moduleDir.resolve("src/main/java");
                        Path testSrcDir = moduleDir.resolve("src/test/java");

                        // Collect packages from main and test sources in each module
                        if (Files.exists(mainSrcDir)) {
                            packages.addAll(findPackagesInDirectory(mainSrcDir));
                        }
                        if (Files.exists(testSrcDir)) {
                            packages.addAll(findPackagesInDirectory(testSrcDir));
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to walk project directory: " + projectDir, e);
        }

        if (packages.isEmpty()) {
            throw new IllegalArgumentException("No package declarations found in source directories");
        }

        return findLongestCommonPrefix(packages);
    }

    private static List<String> findPackagesInDirectory(Path dir) {
        try {
            return Files.walk(dir)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(Packages::extractPackage)
                    .filter(packageName -> packageName != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to walk directory: " + dir, e);
        }
    }

    private static String extractPackage(Path javaFile) {
        try {
            return Files.lines(javaFile)
                    .filter(line -> line.startsWith("package "))
                    .map(line -> line.substring(8, line.indexOf(";")).trim())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            LOG.warnf("Failed to read file: %s", javaFile);
            return null;
        }
    }

    private static Optional<String> findLongestCommonPrefix(List<String> packages) {
        if (packages.isEmpty())
            return Optional.empty();

        // Start with the first package as the initial prefix
        String commonPrefix = packages.get(0);

        // Compare the prefix with each subsequent package and shorten it if necessary
        for (int i = 1; i < packages.size(); i++) {
            commonPrefix = commonPrefix(commonPrefix, packages.get(i));
            if (commonPrefix.isEmpty()) {
                break; // No common prefix found, can exit early
            }
        }

        return commonPrefix.isEmpty() ? Optional.empty() : Optional.of(commonPrefix);
    }

    private static String commonPrefix(String str1, String str2) {
        int minLength = Math.min(str1.length(), str2.length());
        int index = 0;
        while (index < minLength && str1.charAt(index) == str2.charAt(index)) {
            index++;
        }
        return str1.substring(0, index);
    }
}
