package io.quarkiverse.backstage.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GitTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldDescribeProjectNotUnderGit() throws Exception {
        Path projectDir = Files.createDirectories(tempDir.resolve("no-git"));

        assertEquals(Optional.empty(), Git.getUrl(projectDir, "origin", "main", Path.of("catalog-info.yaml")));
        assertEquals(Optional.empty(), Git.findRemoteUrl(projectDir, "origin", true));
        assertTrue(Git.describeMissingUrl(projectDir, "origin").contains("is not a git repository"));
    }

    @Test
    public void shouldDescribeMissingRemote() throws Exception {
        Path projectDir = createRepo("no-remote", "[core]\n\tbare = false\n");

        assertEquals(Optional.empty(), Git.getUrl(projectDir, "origin", "main", Path.of("catalog-info.yaml")));
        assertTrue(Git.describeMissingUrl(projectDir, "origin").contains("No git remote named 'origin'"));
    }

    @Test
    public void shouldDescribeUnsupportedRemote() throws Exception {
        Path projectDir = createRepo("unsupported", "[remote \"origin\"]\n\turl = file:///some/local/repo\n");

        assertEquals(Optional.empty(), Git.getUrl(projectDir, "origin", "main", Path.of("catalog-info.yaml")));
        assertTrue(Git.describeMissingUrl(projectDir, "origin").contains("is not supported"));
    }

    @Test
    public void shouldGetUrlFromProjectInSubdirectory() throws Exception {
        Path repoDir = createRepo("repo", "[remote \"origin\"]\n\turl = git@github.com:acme/app.git\n");
        Path projectDir = Files.createDirectories(repoDir.resolve("app"));

        assertEquals(Optional.of("https://github.com/acme/app.git"), Git.findRemoteUrl(projectDir, "origin", true));
        assertEquals(Optional.of("https://github.com/acme/app/blob/main/catalog-info.yaml"),
                Git.getUrl(projectDir, "origin", "main", Path.of("catalog-info.yaml")));
    }

    private Path createRepo(String name, String config) throws Exception {
        Path dir = tempDir.resolve(name);
        Files.createDirectories(dir.resolve(".git"));
        Files.writeString(dir.resolve(".git").resolve("config"), config);
        return dir;
    }
}
