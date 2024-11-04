package io.quarkiverse.backstage.common.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import io.quarkus.devtools.project.QuarkusProject;

public final class Projects {

    private static final String[] BUILD_FILES = { "pom.xml", "build.gradle", "build.gradle.kts" };

    public static Path getProjectRoot() {
        return getProjectRoot(Paths.get(System.getProperty("user.dir")));

    }

    public static Path getProjectRoot(Path dir) {
        Optional<Path> scmRoot = Git.getScmRoot(dir);
        if (scmRoot.isPresent()) {
            return scmRoot.get();
        }

        //Iterate from current dir to root dir and return the last dir that contained a build file
        Path currentDir = dir;
        while (currentDir != null) {
            for (String buildFile : BUILD_FILES) {
                if (currentDir.resolve(buildFile).toFile().exists()) {
                    return currentDir;
                }
            }
            currentDir = currentDir.getParent();
        }
        return dir;
    }

    public static Map<String, String> getProjectInfo(Path projectDirPath) {
        if (projectDirPath.resolve("pom.xml").toFile().exists()) {
            return Maven.getProjectInfo(projectDirPath);
        } else if (projectDirPath.resolve("build.gradle").toFile().exists()) {
            return Gradle.getProjectInfo(projectDirPath);
        } else if (projectDirPath.resolve("build.gradle.kts").toFile().exists()) {
            return GradleKotlin.getProjectInfo(projectDirPath);
        } else {
            throw new IllegalArgumentException("Unsupported build tool");
        }
    }

    public static Map<String, String> getProjectInfo(QuarkusProject project) {
        switch (project.getBuildTool()) {
            case MAVEN:
                return Maven.getProjectInfo(project);
            case GRADLE:
                return Gradle.getProjectInfo(project);
            case GRADLE_KOTLIN_DSL:
                return GradleKotlin.getProjectInfo(project);
            default:
                throw new IllegalArgumentException("Unsupported build tool: " + project.getBuildTool());
        }
    }
}
