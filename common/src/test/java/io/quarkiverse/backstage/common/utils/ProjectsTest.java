package io.quarkiverse.backstage.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ProjectsTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldGetMavenProjectRoot() throws Exception {
        Path projectRoot = tempDir.resolve("maven-project");
        Path docsDir = projectRoot.resolve("docs");
        Path moduleRoot = projectRoot.resolve("module");
        Path srcMainJavadir = moduleRoot.resolve("src").resolve("main").resolve("java");
        Path targetDir = moduleRoot.resolve("target");

        Files.createDirectories(projectRoot);
        Files.createDirectories(targetDir);
        Files.createDirectories(docsDir);
        Files.createDirectories(srcMainJavadir);

        Files.createFile(projectRoot.resolve("pom.xml"));
        Files.createFile(moduleRoot.resolve("pom.xml"));

        assertEquals(projectRoot, Projects.getProjectRoot(projectRoot));
        assertEquals(projectRoot, Projects.getProjectRoot(targetDir));
        assertEquals(projectRoot, Projects.getProjectRoot(srcMainJavadir));
        assertEquals(projectRoot, Projects.getProjectRoot(docsDir));
    }

    @Test
    public void shouldGetGradleProjectRoot() throws Exception {
        Path projectRoot = tempDir.resolve("gradle-project");
        Path docsDir = projectRoot.resolve("docs");
        Path moduleRoot = projectRoot.resolve("module");
        Path srcMainJavadir = moduleRoot.resolve("src").resolve("main").resolve("java");
        Path buildDir = moduleRoot.resolve("build");

        Files.createDirectories(projectRoot);
        Files.createDirectories(buildDir);
        Files.createDirectories(docsDir);
        Files.createDirectories(srcMainJavadir);

        Files.createFile(projectRoot.resolve("build.gradle"));
        Files.createFile(moduleRoot.resolve("build.gradle"));

        assertEquals(projectRoot, Projects.getProjectRoot(projectRoot));
        assertEquals(projectRoot, Projects.getProjectRoot(buildDir));
        assertEquals(projectRoot, Projects.getProjectRoot(srcMainJavadir));
        assertEquals(projectRoot, Projects.getProjectRoot(docsDir));
    }

    @Test
    public void shouldGetModuleRootForMavenProject() throws Exception {
        Path projectRoot = tempDir.resolve("maven-project");
        Path docsDir = projectRoot.resolve("docs");
        Path moduleRoot = projectRoot.resolve("module");
        Path srcMainJavadir = moduleRoot.resolve("src").resolve("main").resolve("java");
        Path targetDir = moduleRoot.resolve("target");

        Files.createDirectories(projectRoot);
        Files.createDirectories(targetDir);
        Files.createDirectories(docsDir);
        Files.createDirectories(srcMainJavadir);

        Files.createFile(projectRoot.resolve("pom.xml"));
        Files.createFile(moduleRoot.resolve("pom.xml"));

        assertEquals(projectRoot, Projects.getProjectRoot(projectRoot));
        assertEquals(projectRoot, Projects.getProjectRoot(targetDir));
        assertEquals(projectRoot, Projects.getProjectRoot(srcMainJavadir));
        assertEquals(projectRoot, Projects.getProjectRoot(docsDir));
    }

    @Test
    public void shouldGetModuleRootForGradleProject() throws Exception {
        Path projectRoot = tempDir.resolve("gradle-project");
        Path docsDir = projectRoot.resolve("docs");
        Path moduleRoot = projectRoot.resolve("module");
        Path srcMainJavadir = moduleRoot.resolve("src").resolve("main").resolve("java");
        Path buildDir = moduleRoot.resolve("build");

        Files.createDirectories(projectRoot);
        Files.createDirectories(buildDir);
        Files.createDirectories(docsDir);
        Files.createDirectories(srcMainJavadir);

        Files.createFile(projectRoot.resolve("build.gradle"));
        Files.createFile(moduleRoot.resolve("build.gradle"));

        assertEquals(projectRoot, Projects.getProjectRoot(projectRoot));
        assertEquals(projectRoot, Projects.getProjectRoot(buildDir));
        assertEquals(projectRoot, Projects.getProjectRoot(srcMainJavadir));
        assertEquals(projectRoot, Projects.getProjectRoot(docsDir));
    }

    @Test
    public void shouldGetModuleRootForUnknown() throws Exception {
        Path projectRoot = tempDir.resolve("unknown-project");
        Path gitRoot = projectRoot.resolve(".git");
        Path docsDir = projectRoot.resolve("docs");
        Path moduleRoot = projectRoot.resolve("module");
        Path srcMainJavadir = moduleRoot.resolve("src").resolve("main").resolve("java");
        Path targetDir = moduleRoot.resolve("target");

        Files.createDirectories(projectRoot);
        Files.createDirectories(gitRoot);
        Files.createDirectories(targetDir);
        Files.createDirectories(docsDir);
        Files.createDirectories(srcMainJavadir);

        assertEquals(projectRoot, Projects.getProjectRoot(projectRoot));
        assertEquals(projectRoot, Projects.getProjectRoot(targetDir));
        assertEquals(projectRoot, Projects.getProjectRoot(srcMainJavadir));
        assertEquals(projectRoot, Projects.getProjectRoot(docsDir));

        assertEquals(projectRoot, Projects.getModuleRoot(projectRoot));
        assertEquals(projectRoot, Projects.getModuleRoot(targetDir));
        assertEquals(projectRoot, Projects.getModuleRoot(srcMainJavadir));
        assertEquals(projectRoot, Projects.getModuleRoot(docsDir));
    }
}
