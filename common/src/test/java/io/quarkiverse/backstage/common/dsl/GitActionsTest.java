package io.quarkiverse.backstage.common.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.junit.jupiter.api.Test;

public class GitActionsTest {

    @Test
    public void shouldCreateTempRepo() {
        GitActions a = GitActions.createTempo();
        assertNotNull(a);
        assertNotNull(a.getRepositoryDotGitPath());
        assertTrue(a.getRepositoryDotGitPath().toFile().exists());
    }

    @Test
    public void shouldCreateBranch() throws IOException {
        Path path = GitActions.createTempo().createBranch("my-branch").getRepositoryDotGitPath();
        Git git = Git.open(path.toFile());
        String branchName = git.getRepository().getBranch();
        assertEquals("my-branch", branchName);
    }

    @Test
    public void shouldCheckouCreateBranch() throws IOException {
        Path path = GitActions.createTempo().checkoutOrCreateBranch("origin", "my-branch").getRepositoryDotGitPath();
        Git git = Git.open(path.toFile());
        String branchName = git.getRepository().getBranch();
        assertEquals("my-branch", branchName);
    }

    @Test
    public void shouldCommit() throws IOException, NoHeadException, GitAPIException {
        //Make temporary directory
        Path tempDirPath = Files.createTempDirectory("quarkus-backstage-test-");
        Path readmePath = tempDirPath.resolve("README.md");
        Files.write(readmePath, "Hello World".getBytes());
        Git git = GitActions.createTempo().createBranch("my-branch").importFiles(tempDirPath, readmePath)
                .commit("Add readme", readmePath).getGit();
        String headCommit = git.log().call().iterator().next().getName();
        assertEquals("Add readme", git.getRepository().parseCommit(git.getRepository().resolve(headCommit)).getFullMessage());
    }

    @Test
    public void shouldCommitDirectory() throws IOException, NoHeadException, GitAPIException {
        //Make temporary directory
        Path tempDirPath = Files.createTempDirectory("quarkus-backstage-test-");
        Path innerDirPath = tempDirPath.resolve("innerDir");
        Path readmePath = innerDirPath.resolve("README.md");

        innerDirPath.toFile().mkdirs();
        Files.write(readmePath, "Hello World".getBytes());

        Git git = GitActions.createTempo().createBranch("my-branch").importFiles(tempDirPath, readmePath)
                .commit("Add readme", readmePath).getGit();
        String headCommit = git.log().call().iterator().next().getName();
        assertEquals("Add readme", git.getRepository().parseCommit(git.getRepository().resolve(headCommit)).getFullMessage());
    }

    @Test
    public void shouldFilterGitignore() throws IOException, NoHeadException, GitAPIException {
        Path tempDirPath = Files.createTempDirectory("quarkus-backstage-test-");
        Path readmePath = tempDirPath.resolve("README.md");
        Path gitignorePath = tempDirPath.resolve(".gitignore");
        Path backstageDir = tempDirPath.resolve(".backstage");
        Path catalogInfoPath = tempDirPath.resolve("catalog-info.yaml");
        Path backstageFile = backstageDir.resolve("config.yaml");

        Files.write(readmePath, "Hello World".getBytes());
        Files.writeString(gitignorePath, "*.log\n.backstage\ncatalog-info.yaml\nnode_modules/");
        Files.createDirectory(backstageDir);
        Files.writeString(backstageFile, "test");
        Files.writeString(catalogInfoPath, "apiVersion: backstage.io/v1alpha1\nkind: Component");

        Git git = GitActions.createTempo()
                .createBranch("main")
                .importFiles(tempDirPath)
                .getGit();

        Path repoRoot = git.getRepository().getDirectory().toPath().getParent();
        Path filteredGitignore = repoRoot.resolve(".gitignore");
        String content = Files.readString(filteredGitignore);

        assertTrue(content.contains("*.log"), "Should keep *.log pattern");
        assertTrue(content.contains("node_modules/"), "Should keep node_modules/ pattern");
        assertTrue(!content.contains(".backstage"), "Should remove .backstage pattern");
        assertTrue(!content.contains("catalog-info.yaml"), "Should remove catalog-info.yaml pattern");

        assertTrue(Files.exists(repoRoot.resolve(".backstage")), ".backstage directory should be copied");
        assertTrue(Files.exists(repoRoot.resolve("catalog-info.yaml")), "catalog-info.yaml should be copied");
        assertTrue(Files.exists(repoRoot.resolve(".backstage/config.yaml")), ".backstage/config.yaml should be copied");

        git.add().addFilepattern(".").call();
        var status = git.status().call();

        System.out.println("Added files: " + status.getAdded());
        System.out.println("Changed files: " + status.getChanged());

        boolean hasBackstageFiles = status.getAdded().stream().anyMatch(f -> f.startsWith(".backstage/"))
                || status.getChanged().stream().anyMatch(f -> f.startsWith(".backstage/"));
        boolean hasCatalogInfo = status.getAdded().contains("catalog-info.yaml")
                || status.getChanged().contains("catalog-info.yaml");

        assertTrue(hasBackstageFiles, ".backstage files should be staged");
        assertTrue(hasCatalogInfo, "catalog-info.yaml should be staged");
    }
}
