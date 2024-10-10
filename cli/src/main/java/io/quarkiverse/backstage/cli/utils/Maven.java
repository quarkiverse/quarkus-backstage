package io.quarkiverse.backstage.cli.utils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import io.quarkus.devtools.project.QuarkusProject;

public final class Maven {

    public static Map<String, String> getProjectInfo(QuarkusProject project) {
        Map<String, String> mavenInfo = new HashMap<>();
        Path pomPath = project.getProjectDirPath().resolve("pom.xml");
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
}
