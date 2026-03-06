package io.quarkiverse.backstage.common.utils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.logging.Logger;

public final class Directories {

    private static final Logger LOG = Logger.getLogger(Directories.class);

    public static boolean delete(Path dir) {
        try {
            Files.walk(dir)
                    .sorted((path1, path2) -> path2.compareTo(path1))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete file: " + path, e);
                        }
                    });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean copy(Path source, Path destination, Path... excludes) {
        try {
            Set<Path> excludeSet = new HashSet<>(Arrays.asList(excludes));
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (excludeSet.contains(dir)) {
                        LOG.debugf("Skipping excluded dir %s", dir);
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    Path targetDir = destination.resolve(source.relativize(dir));
                    try {
                        if (!Files.exists(targetDir)) {
                            LOG.tracef("Creating directory %s", targetDir);
                            Files.createDirectories(targetDir);
                        }
                    } catch (FileAlreadyExistsException e) {
                        if (!Files.isDirectory(targetDir)) {
                            throw e;
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (excludeSet.contains(file)) {
                        LOG.tracef("Skipping excluded file %s", file);
                        return FileVisitResult.CONTINUE;
                    }
                    Path destinationFile = destination.resolve(source.relativize(file));
                    LOG.tracef("Copying file %s to %s", file, destinationFile);
                    Files.copy(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
