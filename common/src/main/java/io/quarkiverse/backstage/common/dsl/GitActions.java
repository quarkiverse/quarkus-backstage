package io.quarkiverse.backstage.common.dsl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Function;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RefSpec.WildcardMode;
import org.eclipse.jgit.transport.URIish;
import org.jboss.logging.Logger;

import io.quarkiverse.backstage.common.utils.Directories;

public class GitActions {

    private static final Logger LOG = Logger.getLogger(GitActions.class);

    private final Git git;

    private GitActions(Git git) {
        this.git = git;
    }

    public Git getGit() {
        return git;
    }

    public Path getRepsioryPath() {
        return git.getRepository().getDirectory().toPath();
    }

    public static GitActions openRepo(Path path) {
        try {
            Git git = Git.open(path.toFile());
            return new GitActions(git);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize a new git repository at the given path
     *
     * @param path the path to initialize the repository
     * @return a new GitActions instance
     */
    public static GitActions initRepo(Path path) {
        try {
            Git git = Git.init().setDirectory(path.toFile()).call();
            return new GitActions(git);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a temporary git repository
     *
     * @return a new GitActions instance
     */
    public static GitActions createTempo() {
        try {
            Path tempDir = Files.createTempDirectory("quarkus-backstage-git-");
            return initRepo(tempDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add a remote to the repository
     *
     * @param remoteName the name of the remote
     * @param url the url of the remote
     * @return a new GitActions instance
     */
    public GitActions addRemote(String remoteName, String url) {
        try {
            if (git.lsRemote().call().stream().anyMatch(ref -> ref.getName().equals(remoteName))) {
                git.remoteRemove().setRemoteName(remoteName).call();
            }
        } catch (Exception e) {
            // We assume that failure here is due to the remote not existing
        }

        try {
            git.remoteAdd().setName(remoteName).setUri(new URIish(url)).call();
            return new GitActions(git);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GitActions createBranch(String branchName) {
        try {
            if (git.getRepository().resolve("HEAD") == null) {
                git.commit().setMessage("Initial commit").setAllowEmpty(true).call();
            }
            git.checkout().setCreateBranch(true).setName(branchName).call();
            return new GitActions(git);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GitActions checkoutRemoteBranch(String remoteName, String branchName) {
        try {
            git.fetch().setRemote(remoteName).call();
            boolean remoteBranchExists = git.branchList()
                    .setListMode(ListBranchCommand.ListMode.REMOTE)
                    .call()
                    .stream()
                    .anyMatch(ref -> ref.getName().equals("refs/remotes/" + remoteName + "/" + branchName));

            if (remoteBranchExists) {
                git.checkout().setCreateBranch(true)
                        .setName(branchName)
                        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                        .setStartPoint(remoteName + "/" + branchName).call();
                return new GitActions(git);
            }
            throw new RuntimeException("Remote branch " + branchName + " not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GitActions checkoutOrCreateBranch(String remoteName, String branchName) {
        return or(a -> a.checkoutRemoteBranch(remoteName, branchName),
                a -> a.createBranch(branchName));
    }

    public GitActions importFiles(Path sourceRoot, Path... subPaths) {
        Path repositoryRoot = git.getRepository().getDirectory().toPath().getParent();
        try {
            for (Path subPath : subPaths) {
                Path relativeSubPath = subPath.isAbsolute() ? sourceRoot.relativize(subPath) : subPath;
                Path absoluteSubPath = sourceRoot.resolve(relativeSubPath);
                Path destinationPath = repositoryRoot.resolve(relativeSubPath);

                Path destinationParent = destinationPath.getParent();
                if (!destinationParent.toFile().exists()) {
                    Files.createDirectories(destinationParent);
                }

                if (absoluteSubPath.toFile().isDirectory()) {
                    Directories.delete(destinationPath);
                    Directories.copy(absoluteSubPath, destinationPath);
                } else {
                    Files.createDirectories(destinationPath.getParent());
                    Files.copy(absoluteSubPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GitActions commit(String message, Path... paths) {
        try {
            Path repositoryRoot = git.getRepository().getDirectory().toPath().getParent();
            for (Path path : paths) {
                Path destination = repositoryRoot.resolve(path);
                Path relativeDestination = repositoryRoot.relativize(destination);
                Path absoluteDestination = relativeDestination.toAbsolutePath();
                String pattern = absoluteDestination.toFile().isDirectory()
                        ? repositoryRoot.relativize(destination).toString() + File.separator
                        : repositoryRoot.relativize(destination).toString();
                LOG.debugf("Adding %s in git", pattern);
                git.add().addFilepattern(pattern).call();
            }

            RevCommit revComit = git.commit().setMessage(message).call();
            LOG.debugf("Committed %s", revComit.getId().getName());
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public GitActions push(String remoteName, String branchName) {
        try {
            PushCommand command = git.push();
            command.setRemote(remoteName);
            command.setForce(true);
            command.setRefSpecs(
                    new RefSpec("refs/heads/" + branchName + ":refs/heads/" + branchName, WildcardMode.REQUIRE_MATCH));
            command.call();
            return this;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public GitActions push(String remoteName, String branchName, String username, String password) {
        try {
            PushCommand command = git.push();
            command.setRemote(remoteName);
            command.setForce(true);
            command.setRefSpecs(
                    new RefSpec("refs/heads/" + branchName + ":refs/heads/" + branchName, WildcardMode.REQUIRE_MATCH));
            io.quarkiverse.backstage.common.utils.Git.useUserName("quarkus");
            io.quarkiverse.backstage.common.utils.Git.usePassword("quarkus");
            io.quarkiverse.backstage.common.utils.Git.configureCredentials(command);
            command.call();
            return this;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public GitActions or(Function<GitActions, GitActions> left, Function<GitActions, GitActions> right) {
        try {
            return left.apply(this);
        } catch (Exception e) {
            return right.apply(this);
        }
    }
}
