package io.quarkiverse.backstage.common.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MavenTest {

    private Path tempPom;

    @BeforeEach
    public void setup() throws IOException {
        tempPom = Files.createTempFile("pom", ".xml");
        Files.write(tempPom, """
                <project>
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>example-project</artifactId>
                    <version>1.0.0</version>

                    <properties>
                        <compiler-plugin.version>3.13.0</compiler-plugin.version>
                        <maven.compiler.release>21</maven.compiler.release>
                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
                        <quarkus.platform.artifact-id>quarkus-bom</quarkus.platform.artifact-id>
                        <quarkus.platform.group-id>io.quarkus.platform</quarkus.platform.group-id>
                        <quarkus.platform.version>3.16.2</quarkus.platform.version>
                        <skipITs>true</skipITs>
                        <surefire-plugin.version>3.5.0</surefire-plugin.version>
                    </properties>

                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>${quarkus.platform.group-id}</groupId>
                                <artifactId>${quarkus.platform.artifact-id}</artifactId>
                                <version>${quarkus.platform.version}</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>

                    <dependencies>
                      <dependency>
                          <groupId>io.quarkus</groupId>
                          <artifactId>quarkus-arc</artifactId>
                      </dependency>
                      <dependency>
                          <groupId>io.quarkus</groupId>
                          <artifactId>quarkus-rest</artifactId>
                      </dependency>
                    </dependencies>
                </project>
                """.getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempPom);
    }

    @Test
    public void testAddOptionalDependencyWithVersion() throws IOException {
        Maven.addOptionalDependency(tempPom, "my-param", "com.example", "example-artifact", Optional.of("1.0.0"));

        String content = Files.readString(tempPom);
        assertTrue(content.contains("<dependency>"));
        assertTrue(content.contains("<groupId>com.example</groupId>"));
        assertTrue(content.contains("<artifactId>example-artifact</artifactId>"));
        assertTrue(content.contains("<version>1.0.0</version>"));
        assertTrue(content.contains("{%- if values.my-param %}"));
    }

    @Test
    public void testAddOptionalDependencyWithoutVersion() throws IOException {
        Maven.addOptionalDependency(tempPom, "my-param", "com.example", "example-artifact", Optional.empty());

        String content = Files.readString(tempPom);
        assertTrue(content.contains("<dependency>"));
        assertTrue(content.contains("<groupId>com.example</groupId>"));
        assertTrue(content.contains("<artifactId>example-artifact</artifactId>"));
        assertTrue(content.contains("{%- if values.my-param %}"));
    }
}
