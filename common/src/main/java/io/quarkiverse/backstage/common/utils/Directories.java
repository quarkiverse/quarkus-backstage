package io.quarkiverse.backstage.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Directories {

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
}
