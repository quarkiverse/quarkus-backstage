package io.quarkiverse.backstage.common.utils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Helm {

    public static Optional<Path> findHelmDir(Map<Path, String> source) {
        // 1st pass find the .helm directory
        Optional<Path> helmDir = source.keySet().stream().filter(path -> path.endsWith(".helm")).findFirst();
        if (helmDir.isPresent()) {
            return helmDir;
        }

        // 2nd pass find the Chart.yaml file and return its parent parent.
        helmDir = source.keySet().stream().filter(path -> path.endsWith("Chart.yaml")).findFirst()
                .map(Path::getParent)
                .map(Path::getParent);

        return helmDir;
    }

    public static Map<Path, String> parameterize(Map<Path, String> source, Map<String, String> parameters) {
        return findHelmDir(source).map(helmDir -> parameterize(helmDir, source, parameters))
                .orElseThrow(() -> new IllegalArgumentException("No helm directory found"));
    }

    public static Map<Path, String> parameterize(Path helmDir, Map<Path, String> source, Map<String, String> parameters) {
        Map<Path, String> result = new HashMap<>();

        for (Map.Entry<Path, String> entry : source.entrySet()) {
            Path key = helmDir.relativize(entry.getKey());
            String value = entry.getValue();

            Path newKey = key;
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                String to = "${{ values." + parameter.getKey() + " }}";
                String from = parameter.getValue();
                newKey = replace(newKey, from, to);
            }
            result.put(helmDir.resolve(newKey), value);
        }
        return result;
    }

    public static Map<Path, String> parameterize(Map<Path, String> source, String from, String to) {
        Map<Path, String> result = new HashMap<>();
        for (Map.Entry<Path, String> entry : source.entrySet()) {
            Path key = entry.getKey();
            String value = entry.getValue();
            result.put(replace(key, from, to), value);
        }
        return result;
    }

    private static Path replace(Path path, String from, String to) {
        String str = path.toString().replace(from, to);
        return Path.of(str);
    }
}
