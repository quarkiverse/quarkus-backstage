package io.quarkiverse.backstage.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class GithubTest {

    @Test
    public void testCloneUrl() {
        String expectedCloneUrl = "git@github.com:my-org/my-repo.git";
        assertEquals(expectedCloneUrl, Github.toSshCloneUrl("https://github.com/my-org/my-repo.git"));
        assertEquals(expectedCloneUrl, Github.toSshCloneUrl(
                "https://github.com/my-org/my-repo/blob/main/locations/templates/quarkus-application/template.yaml"));
        assertEquals(expectedCloneUrl, Github.toSshCloneUrl(
                "https://raw.githubusercontent.com/my-org/my-repo/refs/heads/main/locations/templates/quarkus-application/template.yaml"));
    }

    @Test
    public void testRelativePathUrl() {
        Path dot = Paths.get(".");
        Path templateYamlPath = Paths.get("locations").resolve("templates").resolve("quarkus-application")
                .resolve("template.yaml");
        assertEquals(dot, Github.toRelativePath("https://github.com/my-org/my-repo.git"));
        assertEquals(templateYamlPath, Github.toRelativePath(
                "https://github.com/my-org/my-repo/blob/main/locations/templates/quarkus-application/template.yaml"));
        assertEquals(templateYamlPath, Github.toRelativePath(
                "https://github.com/my-org/my-repo/blob/other-branch/locations/templates/quarkus-application/template.yaml"));
        assertEquals(templateYamlPath, Github.toRelativePath(
                "https://raw.githubusercontent.com/my-org/my-repo/refs/heads/main/locations/templates/quarkus-application/template.yaml"));
    }
}
