package io.quarkiverse.backstage.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public final class ArgoCD {

    public static Optional<Path> findArgoCDDirectory(Map<Path, String> source) {
        Optional<Path> argoCDRootDir = source.keySet().stream()
                .filter(path -> path.endsWith(".argocd")).findFirst();
        if (argoCDRootDir.isPresent()) {
            return argoCDRootDir;
        }
        // 2nd pass find the appproject yaml file and return its parent parent.
        argoCDRootDir = source.keySet().stream()
                .filter(path -> path.getFileName().toString().startsWith("appproject-")
                        && path.getFileName().toString().endsWith(".yaml"))
                .findFirst()
                .map(Path::getParent);

        return argoCDRootDir;
    }

    public static Map<Path, String> parameterize(Map<Path, String> source, Map<String, String> parameters) {
        return findArgoCDDirectory(source).map(argoCDDir -> Templates.parameterize(argoCDDir, source, parameters))
                .orElseThrow(() -> new IllegalArgumentException("No ArgoCD directory found"));
    }

    public static Optional<Path> getApplicationYamlPath(Map<Path, String> source, String applicationName) {
        return findArgoCDDirectory(source).map(dir -> dir.resolve("application-" + applicationName + ".yaml"));
    }

    public static Path getApplicationYamlPath(Path argoCDRootDir, String applicationName) {
        return argoCDRootDir.resolve("application-" + applicationName + ".yaml");
    }

    public static Optional<String> getRepositoryUrl(Map<Path, String> source, String applicationName) {
        return getApplicationYamlPath(source, applicationName)
                .flatMap(p -> Optional.ofNullable(source.get(p)))
                .map(c -> c.replaceAll(".*repoURL: \"(.*)\".*", "$1"));
    }

    public static Optional<String> getRepositoryUrl(Path argoCdRootDir, String applicationName) {
        Path argoCDApplicationYamlPath = getApplicationYamlPath(argoCdRootDir, applicationName);
        try {
            return Files.readAllLines(argoCDApplicationYamlPath)
                    .stream()
                    .filter(l -> l.matches("^\\s*repoURL: .*"))
                    .map(s -> s.replaceAll("^\\s*repoURL:\\s*", ""))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
