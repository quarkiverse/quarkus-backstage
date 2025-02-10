package io.quarkiverse.backstage.common.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static List<Path> listTemplatePaths(Map<Path, String> source) {
        return findHelmDir(source)
                .map(helmDir -> listTemplatePaths(helmDir, source))
                .orElseThrow(() -> new IllegalArgumentException("No helm directory found"));
    }

    public static List<Path> listTemplatePaths(Path helmDir, Map<Path, String> source) {
        List<Path> charts = new ArrayList<>();
        for (Map.Entry<Path, String> entry : source.entrySet()) {
            Path path = entry.getKey();
            if (path.startsWith(helmDir) && path.getParent().getFileName().toString().equals("templates")
                    && (path.getFileName().toString().endsWith(".yaml") || path.getFileName().toString().endsWith(".yml"))) {
                charts.add(path);
            }
        }
        return charts;
    }

    public static List<Path> listValuesYamlPaths(Map<Path, String> source) {
        return findHelmDir(source)
                .map(helmDir -> listValuesYamlPaths(helmDir, source))
                .orElseThrow(() -> new IllegalArgumentException("No helm directory found"));
    }

    public static List<Path> listValuesYamlPaths(Path helmDir, Map<Path, String> source) {
        List<Path> valuesYamlPaths = new ArrayList<>();
        for (Map.Entry<Path, String> entry : source.entrySet()) {
            Path path = entry.getKey();
            if (path.startsWith(helmDir) && (path.getFileName().toString().equals("values.yaml")
                    || path.getFileName().toString().equals("values.yml"))) {
                valuesYamlPaths.add(path);
            }
        }
        return valuesYamlPaths;
    }

    public static Map<Path, String> parameterize(Map<Path, String> source, Map<String, String> parameters) {
        return findHelmDir(source).map(helmDir -> Templates.parameterize(helmDir, source, parameters))
                .orElseThrow(() -> new IllegalArgumentException("No helm directory found"));
    }

    private static boolean isValidChartDir(File chartDir) {
        return isValidChartDir(chartDir.toPath());
    }

    private static boolean isValidChartDir(Path chartDir) {
        return chartDir.toFile().isDirectory() && chartDir.resolve("templates").toFile().exists()
                && chartDir.resolve("Chart.yaml").toFile().exists();
    }

    public static Map<String, Object> parameterize(Map<String, Object> source) {
        return parameterize("helm", source);
    }

    public static Map<String, Object> parameterize(String prefix, Map<String, Object> source) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map m) {
                result.put(key, parameterize(prefix + Strings.capitalizeFirst(key), m));
            } else {
                String valuesKey = prefix + Strings.capitalizeFirst(key);
                System.out.println(key + " -> " + valuesKey + "( was " + value + ")");
                result.put(key, "${{ values." + valuesKey + " }}");
            }
        }
        return result;
    }
}
