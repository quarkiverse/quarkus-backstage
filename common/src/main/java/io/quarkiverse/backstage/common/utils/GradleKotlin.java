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

public final class GradleKotlin {

    private static final String CONDITIONAL_DEPENDENCY = """
              {%%- if values.%s %%}
                  %s("%s:%s")
              {%%- endif %%}
            """;

    private static final String CONDITIONAL_DEPENDENCY_WITH_VERSION = """
              {%%- if values.%s %%}
                  %s("%s:%s:%s")
              {%%- endif %%}
            """;

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

    /**
     * Adds a dependency to the build.gradle.kts file.
     *
     * @param gradleFilePath the path to the build.gradle.kts file
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
            System.out.println("Dependency added successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Error writing to the build.gradle.kts file", e);
        }
    }

    /**
     * Adds a dependency to the build.gradle.kts file.
     *
     * @param content the content to the build.gradle.kts file
     * @param parameter the parameter to check for the dependency
     * @param groupId the groupId of the dependency
     * @param artifactId the artifactId of the dependency
     * @param version the {@link Optional} version of the dependency
     */
    public static String addOptionalDependency(String content, String parameter, String groupId, String artifactId,
            Optional<String> version) {

        String[] lines = content.split("\n|\r");
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

        // If no dependencies section was found, add one with the new dependency at the end
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
