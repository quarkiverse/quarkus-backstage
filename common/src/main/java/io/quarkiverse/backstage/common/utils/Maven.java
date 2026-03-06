package io.quarkiverse.backstage.common.utils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import io.quarkus.devtools.project.QuarkusProject;

public final class Maven {

    private static final String CONDITIONAL_DEPENDENCY = """
              {%%- if values.%s %%}
              <dependency>
                  <groupId>%s</groupId>
                  <artifactId>%s</artifactId>
              </dependency>
              {%%- endif %%}
            """;

    private static final String CONDITIONAL_DEPENDENCY_WITH_VERSION = """
              {%%- if values.%s %%}
              <dependency>
                  <groupId>%s</groupId>
                  <artifactId>%s</artifactId>
                  <version>%s</version>
              </dependency>
              {%%- endif %%}
            """;

    public static Map<String, String> getProjectInfo(QuarkusProject project) {
        return getProjectInfo(project.getProjectDirPath());
    }

    public static Map<String, String> getProjectInfo(Path projectDirPath) {
        Map<String, String> mavenInfo = new HashMap<>();
        Path pomPath = projectDirPath.resolve("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try (FileReader fileReader = new FileReader(pomPath.toFile())) {
            var model = reader.read(fileReader);
            mavenInfo.put("groupId", model.getGroupId());
            mavenInfo.put("artifactId", model.getArtifactId());
            mavenInfo.put("version", model.getVersion());
            if (model.getName() != null) {
                mavenInfo.put("name", model.getName());
            }
            if (model.getDescription() != null) {
                mavenInfo.put("description", model.getDescription());
            }
            return mavenInfo;
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException("Failed to read pom.xml", e);
        }
    }

    /**
     * Add a dependency to the pom.xml file.
     *
     * @param pomXmlPath the path to the pom.xml file
     * @param groupId the groupId of the dependency
     * @param artifactId the artifactId of the dependency
     * @param version the {@link Optional} version of the dependency
     */
    public static void addOptionalDependency(Path pomXmlPath, String parameter, String groupId, String artifactId,
            Optional<String> version) {
        try {
            Files.writeString(pomXmlPath,
                    addOptionalDependency(Files.readString(pomXmlPath), parameter, groupId, artifactId, version));
        } catch (IOException e) {
            throw new RuntimeException("Error writing to the pom.xml", e);
        }
    }

    public static String addOptionalDependency(String content, String parameter, String groupId, String artifactId,
            Optional<String> version) {
        String[] lines = content.split("\n|\r");
        String dependencyBlock = version.isPresent()
                ? String.format(CONDITIONAL_DEPENDENCY_WITH_VERSION, parameter, groupId, artifactId, version.get())
                : String.format(CONDITIONAL_DEPENDENCY, parameter, groupId, artifactId);

        List<String> modifiedLines = new ArrayList<>();
        boolean inDependencyManagement = false;
        boolean dependenciesSectionFound = false;
        boolean dependencyInserted = false;

        for (String line : lines) {
            if (line.trim().equals("<dependencyManagement>")) {
                inDependencyManagement = true;
                modifiedLines.add(line);
            } else if (line.trim().equals("</dependencyManagement>")) {
                inDependencyManagement = false;
                modifiedLines.add(line);
            } else if (!inDependencyManagement && line.trim().equals("<dependencies>")) {
                dependenciesSectionFound = true;
                modifiedLines.add(line);
            } else if (dependenciesSectionFound && line.trim().equals("</dependencies>")) {
                modifiedLines.add(dependencyBlock);
                modifiedLines.add(line);
                dependencyInserted = true;
                dependenciesSectionFound = false;
            } else {
                modifiedLines.add(line);
            }
        }

        if (!dependencyInserted) {
            modifiedLines.add("<dependencies>");
            modifiedLines.add(dependencyBlock);
            modifiedLines.add("</dependencies>");
        }

        return String.join(System.lineSeparator(), modifiedLines);
    }
}
