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
        assertNotNull(a.getRepsioryPath());
        assertTrue(a.getRepsioryPath().toFile().exists());
    }

    @Test
    public void shouldCreateBranch() throws IOException {
        Path path = GitActions.createTempo().createBranch("my-branch").getRepsioryPath();
        Git git = Git.open(path.toFile());
        String branchName = git.getRepository().getBranch();
        assertEquals("my-branch", branchName);
    }

    @Test
    public void shouldCheckouCreateBranch() throws IOException {
        Path path = GitActions.createTempo().checkoutOrCreateBranch("origin", "my-branch").getRepsioryPath();
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
}
