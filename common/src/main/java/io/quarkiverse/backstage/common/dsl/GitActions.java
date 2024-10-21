package io.quarkiverse.backstage.common.dsl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Function;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;

import io.quarkiverse.backstage.common.utils.Directories;

public class GitActions {

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
        Path repositoryRoot = git.getRepository().getDirectory().toPath();
        try {
            for (Path subPath : subPaths) {
                Path relativeSubPath = sourceRoot.relativize(subPath);
                Path destinationPath = repositoryRoot.resolve(relativeSubPath);

                Path destinationParent = destinationPath.getParent();
                if (!destinationParent.toFile().exists()) {
                    Files.createDirectories(destinationParent);
                }

                if (destinationPath.toFile().isDirectory()) {
                    Directories.delete(destinationPath);
                    Directories.copy(subPath, destinationPath);
                } else {
                    Files.copy(subPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GitActions commit(String message, Path root, Path... paths) {
        for (Path p : paths) {
            if (p.isAbsolute()) {
                throw new IllegalArgumentException("Paths must be relative to the project root");
            }
        }

        try {
            Path repositoryRoot = git.getRepository().getDirectory().toPath().getParent();
            for (Path path : paths) {
                Path destination = repositoryRoot.resolve(path);
                if (destination.toFile().isDirectory()) {
                    Directories.delete(destination);
                }
                Files.createDirectories(destination.getParent());
                if (path.toFile().isDirectory()) {
                    Directories.copy(path, destination);
                } else {
                    if (!destination.getParent().toFile().exists()) {
                        destination.getParent().toFile().mkdirs();
                    }
                    Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                }
                git.add().addFilepattern(repositoryRoot.relativize(destination).toString()).call();
            }

            git.commit().setMessage(message).call();
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public GitActions or(Function<GitActions, GitActions> left, Function<GitActions, GitActions> right) {
        try {
            return left.apply(this);
        } catch (Exception e) {
            return right.apply(this);
        }
    }
}
