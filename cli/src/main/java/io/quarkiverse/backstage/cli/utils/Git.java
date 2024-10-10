package io.quarkiverse.backstage.cli.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final String ORIGIN = "origin";

    private static final String GITHUB_PATTERN = "(?:git@|https://)github.com[:/](.*?)/(.*?)(?:.git)?$";
    private static final String GITLAB_PATTERN = "(?:git@|https://)gitlab.com[:/](.*?)/(.*?)(?:.git)?$";
    private static final String BITBUCKET_PATTERN = "(?:git@|https://)bitbucket.org[:/](.*?)/(.*?)(?:.git)?$";
    private static final String GITEA_PATTERN = "(?:git@|https://)(.*?)/gitea/(.*?)/(.*?)(?:.git)?$";

    private static final String GITHUB_HOST = "github.com";
    private static final String GITLAB_HOST = "gitlab.com";
    private static final String BITBUCKET_HOST = "bitbucket.org";

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

    public static Optional<Path> getScmRoot(Path dir) {
        while (dir != null && !dir.resolve(DOT_GIT).toFile().exists()) {
            dir = dir.getParent();
        }
        return Optional.ofNullable(dir).filter(p -> p.resolve(DOT_GIT).toFile().exists());
    }

    private static String buildUrl(String host, String user, String repo, String branch, Path path) {
        String pathStr = path.toString().replace("\\", "/"); // Ensure the path uses forward slashes
        return String.format("https://%s/%s/%s/blob/%s/%s", host, user, repo, branch, pathStr);
    }

    public static Optional<String> getUrl(String remoteName, String branch, Path path) {
        return getScmRoot()
                .flatMap(root -> getScmUrl(root, remoteName).flatMap(baseUrl -> getUrlFromBase(baseUrl, branch, path)));
    }

    public static Optional<String> getUrlFromBase(String baseUrl, String branch, Path path) {
        Pattern githubPattern = Pattern.compile(GITHUB_PATTERN);
        Pattern gitlabPattern = Pattern.compile(GITLAB_PATTERN);
        Pattern bitbucketPattern = Pattern.compile(BITBUCKET_PATTERN);
        Pattern giteaPattern = Pattern.compile(GITEA_PATTERN);

        Matcher matcher;

        if ((matcher = githubPattern.matcher(baseUrl)).matches()) {
            return Optional.of(buildUrl(GITHUB_HOST, matcher.group(1), matcher.group(2), branch, path));
        } else if ((matcher = gitlabPattern.matcher(baseUrl)).matches()) {
            return Optional.of(buildUrl(GITLAB_HOST, matcher.group(1), matcher.group(2), branch, path));
        } else if ((matcher = bitbucketPattern.matcher(baseUrl)).matches()) {
            return Optional.of(buildUrl(BITBUCKET_HOST, matcher.group(1), matcher.group(2), branch, path));
        } else if ((matcher = giteaPattern.matcher(baseUrl)).matches()) {
            return Optional.of(buildUrl(matcher.group(1), matcher.group(2), matcher.group(3), branch, path));
        }

        // Return empty if the remote does not match any known patterns
        return Optional.empty();
    }

    public static Optional<String> getScmUrl() {
        return getScmRoot().flatMap(Git::getScmUrl);
    }

    public static Optional<String> getScmUrl(Path root) {
        return getScmUrl(root, ORIGIN);
    }

    public static Optional<String> getScmUrl(Path root, String remoteName) {
        try {
            return io.dekorate.utils.Git.getSafeRemoteUrl(root, remoteName);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getScmBranch(Path path) {
        try {
            return io.dekorate.utils.Git.getBranch(path);
        } catch (Exception e) {
            return Optional.empty();
        }
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

    public static boolean commit(String branch, String message, Path... paths) {
        return getScmRoot().map(root -> {
            try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(root.toFile())) {
                // Check if the branch exists
                boolean branchExists = git.getRepository().findRef("refs/heads/" + branch) != null;

                // If branch does not exist, create it
                if (!branchExists) {
                    git.branchCreate().setName(branch).call();
                }

                // Checkout the branch (create if needed)
                git.checkout().setName(branch).setCreateBranch(!branchExists).call();

                // Add all specified files
                for (Path path : paths) {
                    git.add().addFilepattern(root.relativize(path).toString()).call();
                }

                // Commit the changes
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
                command.setForce(true);
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
