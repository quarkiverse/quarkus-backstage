package io.quarkiverse.backstage.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Strings {

    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String defaultIfEmpty(String str, String defaultStr) {
        return isNullOrEmpty(str) ? defaultStr : str;
    }

    public static String join(final Object[] array) {
        return join(array, ',');
    }

    public static String join(final Object[] array, final char separator) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return "";
        }
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String toCamelCase(String dotSeparated) {
        StringBuilder result = new StringBuilder();
        String[] parts = dotSeparated.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == 0) {
                // Keep the first word lowercase
                result.append(part.toLowerCase());
            } else {
                // Capitalize the first letter of subsequent words
                result.append(part.substring(0, 1).toUpperCase());
                result.append(part.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    public static String read(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String read(InputStream is) {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeStringSafe(Path p, String content) {
        try {
            if (!Files.exists(p.getParent())) {
                Files.createDirectories(p.getParent());
            }
            Files.writeString(p, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
