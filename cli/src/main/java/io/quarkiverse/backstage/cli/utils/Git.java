package io.quarkiverse.backstage.cli.utils;;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RefSpec.WildcardMode;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.microprofile.config.ConfigProvider;

public final class Git {

    public static final String DOT_GIT = ".git";
    private static String username;
    private static String password;

    private Git() {
        //Utility class
    }

    public static void useUserName(String username) {
        Git.username = username;
    }

    public static void usePassword(String password) {
        Git.password = password;
    }

    public static void configureCredentials() {
        ConfigProvider.getConfig().getOptionalValue("quarkus.backstage.git.username", String.class)
                .ifPresent(s -> {
                    Git.useUserName(s);
                });

        ConfigProvider.getConfig().getOptionalValue("quarkus.backstage.git.password", String.class)
                .ifPresent(s -> {
                    Git.usePassword(s);
                });

    }

    /**
     * Checks if a Git repository exists at the provided URL.
     *
     * @param url The URL of the Git repository.
     * @return true if the repository exists, false otherwise.
     */
    public static boolean checkIfRepoExists(String url) {
        try {
            LsRemoteCommand lsRemoteCommand = org.eclipse.jgit.api.Git.lsRemoteRepository();
            configureAuth(lsRemoteCommand, url);
            lsRemoteCommand.setRemote(url).call();
            return true;
        } catch (TransportException e) {
            return false;
        } catch (GitAPIException e) {
            return false;
        }
    }

    public static boolean hasUncommittedChanges() {
        return getScmRoot().map(root -> {
            try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(root.toFile())) {
                return git.status().call().isClean();
            } catch (IOException | GitAPIException e) {
                return false;
            }
        }).orElse(false);
    }

    public static boolean commit(String message, Path... paths) {
        return getScmRoot().map(root -> {
            try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(root.toFile())) {
                for (Path path : paths) {
                    git.add().addFilepattern(path.toString()).call();
                }
                git.commit().setMessage(message).call();
                return true;
            } catch (IOException | GitAPIException e) {
                return false;
            }
        }).orElse(false);
    }

    public static boolean push(String remoteName, String branch) {
        return getScmRoot().map(root -> {
            try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(root.toFile())) {
                PushCommand command = git.push();
                command.setRemote(remoteName);
                command.setRefSpecs(new RefSpec("refs/heads/" + branch + ":refs/heads/" + branch, WildcardMode.REQUIRE_MATCH));
                command.call();
                return true;
            } catch (IOException | GitAPIException e) {
                return false;
            }
        }).orElse(false);
    }

    public static boolean hasUnpushedChanges(String url) {
        return getScmRoot().map(root -> {
            try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(root.toFile())) {
                String remoteName = getRemoteName(url).orElse("origin");
                Repository repository = git.getRepository();
                FetchCommand fetchCommand = git.fetch();
                configureAuth(fetchCommand, url);
                FetchResult fetchResult = fetchCommand.call();

                Ref localRef = repository.findRef("refs/heads/" + repository.getBranch());
                Ref trackingRef = repository.findRef("refs/remotes/" + remoteName + "/" + repository.getBranch());

                if (trackingRef == null) {
                    // No remote branch to compare, so assume there are unpushed commits
                    return true;
                }

                ObjectId localCommitId = localRef.getObjectId();
                ObjectId trackingCommitId = trackingRef.getObjectId();

                if (!localCommitId.equals(trackingCommitId)) {
                    RevWalk walk = new RevWalk(repository);
                    RevCommit localCommit = walk.parseCommit(localCommitId);
                    RevCommit trackingCommit = walk.parseCommit(trackingCommitId);

                    return walk.isMergedInto(trackingCommit, localCommit);
                }
                return false;
            } catch (IOException | GitAPIException e) {
                return false;
            }
        }).orElse(false);
    }

    public static void configureAuth(TransportCommand command, String url) {
        if (isGithubSshUrl(url)) {
            condigureSsh(command);
        }
        if (username != null && password != null) {
            configureCredentials(command);
        }
    }

    public static void condigureSsh(TransportCommand command) {
        command.setTransportConfigCallback(transport -> {
            SshSessionFactory sshSessionFactory = JschConfigSessionFactory.getInstance();
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        });
    }

    public static void configureCredentials(TransportCommand command) {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
        command.setCredentialsProvider(credentialsProvider);
    }

    public static boolean isGithubSshUrl(String url) {
        return url.startsWith("git@github.com:");
    }

    public static Optional<String> getRemoteName(String url) {
        return getScmRoot().flatMap(root -> {
            try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(root.toFile())) {
                for (String remoteName : git.getRepository().getRemoteNames()) {
                    String remoteUrl = git.getRepository().getConfig().getString("remote", remoteName, "url");
                    if (remoteUrl.equals(url)) {
                        return Optional.of(remoteName);
                    }
                }
                return Optional.<String> empty();
            } catch (IOException e) {
                return Optional.<String> empty();
            }
        });
    }

    /**
     * Get the git remote urls as a map.
     *
     * @param path the path to the git config.
     * @return A {@link Map} of urls per remote.
     */
    public static Map<String, String> getRemotes(Path path) {
        return io.dekorate.utils.Git.getRemotes(path);
    }

    public static Optional<Path> getScmRoot() {
        Path dir = Paths.get("").toAbsolutePath();
        while (dir != null && !dir.resolve(DOT_GIT).toFile().exists()) {
            dir = dir.getParent();
        }
        return Optional.ofNullable(dir).filter(p -> p.resolve(DOT_GIT).toFile().exists());
    }
}
