package io.quarkiverse.backstage.deployment.utils;

import java.nio.file.Path;
import java.util.Map;

import io.quarkus.devtools.project.QuarkusProject;

public final class Projects {

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
