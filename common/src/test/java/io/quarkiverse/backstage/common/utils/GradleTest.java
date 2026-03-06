package io.quarkiverse.backstage.common.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GradleTest {

    private Path tempGradleFile;

    @BeforeEach
    public void setup() throws IOException {
        tempGradleFile = Files.createTempFile("build", ".gradle");
        Files.write(tempGradleFile,
                """
                        plugins {
                            id 'java'
                            id 'io.quarkus'
                        }

                        repositories {
                            mavenCentral()
                            mavenLocal()
                        }

                        dependencies {
                            implementation 'org.apache.commons:commons-lang3:3.12.0'
                            implementation enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}")
                            implementation 'io.quarkus:quarkus-arc'
                            implementation 'io.quarkus:quarkus-rest'
                            testImplementation 'io.quarkus:quarkus-junit5'
                            testImplementation 'io.rest-assured:rest-assured'
                        }

                        group 'org.acme'
                        version '1.0.0-SNAPSHOT'
                        """
                        .getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempGradleFile);
    }

    @Test
    public void testAddOptionalDependencyWithVersion() throws IOException {
        Gradle.addOptionalDependency(tempGradleFile, "my-param", "com.example", "example-artifact", Optional.of("1.0.0"));

        String content = Files.readString(tempGradleFile);
        System.out.println(content);
        assertTrue(content.contains("dependencies {"));
        assertTrue(content.contains("implementation 'com.example:example-artifact:1.0.0'"));
        assertTrue(content.contains("{%- if values.my-param %}"));
    }

    @Test
    public void testAddOptionalDependencyWithoutVersion() throws IOException {
        Gradle.addOptionalDependency(tempGradleFile, "my-param", "com.example", "example-artifact", Optional.empty());

        String content = Files.readString(tempGradleFile);
        assertTrue(content.contains("dependencies {"));
        assertTrue(content.contains("implementation 'com.example:example-artifact'"));
        assertTrue(content.contains("{%- if values.my-param %}"));
    }
}
