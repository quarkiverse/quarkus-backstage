package io.quarkiverse.backstage.cli.utils;

import java.util.Map;

import io.quarkus.devtools.project.QuarkusProject;

public final class Projects {

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
