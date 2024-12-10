package io.quarkiverse.backstage.common.utils;

import java.nio.file.Path;
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
        return findHelmDir(source).map(helmDir -> Templates.parameterize(helmDir, source, parameters))
                .orElseThrow(() -> new IllegalArgumentException("No helm directory found"));
    }

}
