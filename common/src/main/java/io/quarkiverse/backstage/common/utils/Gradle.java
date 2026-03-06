package io.quarkiverse.backstage.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.quarkus.devtools.project.QuarkusProject;

public final class Gradle {
    private static final String CONDITIONAL_DEPENDENCY = """
              {%%- if values.%s %%}
                  %s '%s:%s'
              {%%- endif %%}
            """;

    private static final String CONDITIONAL_DEPENDENCY_WITH_VERSION = """
              {%%- if values.%s %%}
                  %s '%s:%s:%s'
              {%%- endif %%}
            """;

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

    /**
     * Adds a dependency to the build.gradle file.
     *
     * @param gradleFilePath the path to the build.gradle file
     * @param parameter the parameter to check for the dependency
     * @param groupId the groupId of the dependency
     * @param artifactId the artifactId of the dependency
     * @param version the {@link Optional} version of the dependency
     */
    public static void addOptionalDependency(Path gradleFilePath, String parameter, String groupId, String artifactId,
            Optional<String> version) {
        try {
            Files.writeString(gradleFilePath,
                    addOptionalDependency(Strings.read(gradleFilePath), parameter, groupId, artifactId, version));
        } catch (IOException e) {
            throw new RuntimeException("Error writing to the build.gradle file", e);
        }
    }

    /**
     * Adds a dependency to the build.gradle file.
     *
     * @param content the content to the build.gradle file
     * @param parameter the parameter to check for the dependency
     * @param groupId the groupId of the dependency
     * @param artifactId the artifactId of the dependency
     * @param version the {@link Optional} version of the dependency
     * @return the updated build.gradle
     */
    public static String addOptionalDependency(String content, String parameter, String groupId, String artifactId,
            Optional<String> version) {
        String[] lines = content.split("\n|\r");

        // Construct the dependency line based on version availability
        String dependencyLine = version.isPresent()
                ? String.format(CONDITIONAL_DEPENDENCY_WITH_VERSION, parameter, "implementation", groupId, artifactId,
                        version.get())
                : String.format(CONDITIONAL_DEPENDENCY, parameter, "implementation", groupId, artifactId);

        List<String> modifiedLines = new ArrayList<>();
        boolean dependenciesSectionFound = false;
        boolean dependencyInserted = false;

        for (String line : lines) {
            if (line.trim().equals("dependencies {")) {
                dependenciesSectionFound = true;
                modifiedLines.add(line);
            } else if (dependenciesSectionFound && line.trim().equals("}")) {
                // Insert dependency just before the closing bracket of dependencies block
                modifiedLines.add(dependencyLine);
                modifiedLines.add(line);
                dependencyInserted = true;
                dependenciesSectionFound = false;
            } else {
                modifiedLines.add(line);
            }
        }

        // If no dependencies section was found, add it with the new dependency
        if (!dependencyInserted) {
            modifiedLines.add("dependencies {");
            modifiedLines.add(dependencyLine);
            modifiedLines.add("}");
        }

        return String.join(System.lineSeparator(), modifiedLines);
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
