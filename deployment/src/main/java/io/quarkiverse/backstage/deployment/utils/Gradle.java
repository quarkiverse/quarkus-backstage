package io.quarkiverse.backstage.deployment.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.quarkus.devtools.project.QuarkusProject;

public final class Gradle {

    public static Map<String, String> getProjectInfo(QuarkusProject project) {
        return getProjectInfo(project.getProjectDirPath());
    }

    public static Map<String, String> getProjectInfo(Path projectDirPath) {
        Map<String, String> gradleInfo = new HashMap<>();
        Path buildGradlePath = projectDirPath.resolve("build.gradle");
        Path settingsGradlePath = projectDirPath.resolve("settings.gradle");

        try {
            // First, try extracting the project name from settings.gradle
            if (settingsGradlePath.toFile().exists()) {
                String settingsGradleContent = readFile(settingsGradlePath);
                String projectName = extractFromPattern(settingsGradleContent, "rootProject.name\\s*=\\s*['\"](.*?)['\"]");
                if (projectName != null) {
                    gradleInfo.put("name", projectName);
                }
            }

            // Then, try extracting groupId and version from build.gradle
            if (buildGradlePath.toFile().exists()) {
                String buildGradleContent = readFile(buildGradlePath);
                String groupId = extractFromPattern(buildGradleContent, "group\\s*=\\s*['\"](.*?)['\"]");
                String version = extractFromPattern(buildGradleContent, "version\\s*=\\s*['\"](.*?)['\"]");

                gradleInfo.put("groupId", groupId != null ? groupId : "");
                gradleInfo.put("version", version != null ? version : "");

                // If name wasn't found in settings.gradle, try to get it from build.gradle
                if (!gradleInfo.containsKey("name")) {
                    String projectName = extractFromPattern(buildGradleContent, "rootProject.name\\s*=\\s*['\"](.*?)['\"]");
                    gradleInfo.put("name",
                            projectName != null ? projectName : projectDirPath.getFileName().toString());
                }
            }

            return gradleInfo;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read build.gradle or settings.gradle", e);
        }
    }

    private static String readFile(Path filePath) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(filePath));
    }

    private static String extractFromPattern(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
