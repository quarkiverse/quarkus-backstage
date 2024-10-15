package io.quarkiverse.backstage.deployment.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.quarkus.devtools.project.QuarkusProject;

public final class GradleKotlin {

    public static Map<String, String> getProjectInfo(QuarkusProject project) {
        return getProjectInfo(project.getProjectDirPath());
    }

    public static Map<String, String> getProjectInfo(Path projectDirPath) {
        Map<String, String> gradleKotlinInfo = new HashMap<>();
        Path buildGradleKtsPath = projectDirPath.resolve("build.gradle.kts");
        Path settingsGradleKtsPath = projectDirPath.resolve("settings.gradle.kts");

        try {
            // First, try extracting the project name from settings.gradle.kts
            if (settingsGradleKtsPath.toFile().exists()) {
                String settingsGradleKtsContent = readFile(settingsGradleKtsPath);
                String projectName = extractFromPattern(settingsGradleKtsContent, "rootProject.name\\s*=\\s*\"(.*?)\"");
                if (projectName != null) {
                    gradleKotlinInfo.put("name", projectName);
                }
            }

            // Then, try extracting groupId and version from build.gradle.kts
            if (buildGradleKtsPath.toFile().exists()) {
                String buildGradleKtsContent = readFile(buildGradleKtsPath);
                String groupId = extractFromPattern(buildGradleKtsContent, "group\\s*=\\s*\"(.*?)\"");
                String version = extractFromPattern(buildGradleKtsContent, "version\\s*=\\s*\"(.*?)\"");

                gradleKotlinInfo.put("groupId", groupId != null ? groupId : "");
                gradleKotlinInfo.put("version", version != null ? version : "");

                // If name wasn't found in settings.gradle.kts, try to get it from build.gradle.kts
                if (!gradleKotlinInfo.containsKey("name")) {
                    String projectName = extractFromPattern(buildGradleKtsContent, "rootProject.name\\s*=\\s*\"(.*?)\"");
                    gradleKotlinInfo.put("name",
                            projectName != null ? projectName : projectDirPath.getFileName().toString());
                }
            }

            return gradleKotlinInfo;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read build.gradle.kts or settings.gradle.kts", e);
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
