package io.quarkiverse.backstage.common.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.ListBranchCommand;
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
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

public class Git {

    private static final Logger LOG = Logger.getLogger(Git.class);

    public static final String DOT_GIT = ".git";
    public static final String CONFIG = "config";
    public static final String ORIGIN = "origin";
    public static final String OB = "[";
    public static final String CB = "]";
    public static final String SLASH = "/";
    public static final String COLN = ":";
    public static final String EQUALS = "=";
    public static final String REMOTE = "remote";
    public static final String HEAD = "HEAD";
    public static final String URL = "url";
    public static final String REF = "ref";

    public static final String REMOTE_PATTERN = "^\\s*\\[remote\\s*\"([a-zA-Z0-9_-]+)\"\\s*\\]\\s*";

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

    public static Optional<String> getScmUrl(Path root, String remote) {
        return getScmUrl(root, remote, true);
    }

    public static Optional<String> getScmUrl(Path root, String remote, boolean httpsPreferred) {
        try {
            Optional<String> url = Files.lines(getConfig(root)).map(String::trim)
                    .filter(inRemote(remote, new AtomicBoolean()))
                    .filter(l -> l.startsWith(URL) && l.contains(EQUALS))
                    .map(s -> s.split(EQUALS)[1].trim())
                    .findAny();
            return httpsPreferred ? url.map(Git::sanitizeRemoteUrl) : url;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getScmBranch(Path path) {
        try {
            return Files.lines(getHead(path)).map(String::trim)
                    .filter(l -> l.startsWith(REF) && l.contains(SLASH))
                    .map(s -> s.substring(s.lastIndexOf(SLASH) + 1).trim())
                    .findAny();
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

    public static Optional<Path> createTempRepo(String remote) {
        return getScmRoot().map(root -> {
            return getScmUrl(root, remote, false).map(url -> {
                try {
                    Path tempDir = Files.createTempDirectory("quarkus-backstage-git-");
                    org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.init().setDirectory(tempDir.toFile()).call();
                    git.remoteAdd().setName(remote).setUri(new URIish(url)).call();
                    LOG.infof("Created temporary repository at %s for remote %s: %s", tempDir, remote, url);
                    return tempDir;
                } catch (IOException | GitAPIException | URISyntaxException e) {
                    return null;
                }
            }).orElse(null);
        });
    }

    /**
     * Copy the specified paths to a repository and commit them.
     *
     * @param repositoryRoot The root of the repository.
     * @param branch The branch to commit to.
     * @param message The commit message.
     * @param paths The paths to copy.
     *
     * @return true if the commit was successful, false otherwise.
     */
    public static boolean commit(Path repositoryRoot, String branch, String message, Path... paths) {
        for (Path p : paths) {
            if (p.isAbsolute()) {
                throw new IllegalArgumentException("Paths must be relative to the project root");
            }
        }

        try {
            org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(repositoryRoot.toFile());
            String remoteName = RemoteConfig.getAllRemoteConfigs(git.getRepository().getConfig()).stream()
                    .map(RemoteConfig::getName).findFirst().orElse("origin");

            git.fetch().setRemote(remoteName).call();

            boolean remoteBranchExists = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE)
                    .call().stream().anyMatch(ref -> ref.getName().equals("refs/remotes/" + remoteName + "/" + branch));

            if (remoteBranchExists) {
                git.checkout().setCreateBranch(true)
                        .setName(branch)
                        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                        .setStartPoint(remoteName + "/" + branch).call();
            } else {
                git.commit().setMessage("Initial commit").setAllowEmpty(true).call();
                git.checkout().setCreateBranch(true).setOrphan(true).setName(branch).call();
            }

            for (Path path : paths) {
                Path destination = repositoryRoot.resolve(path);
                if (destination.toFile().isDirectory()) {
                    Directories.delete(destination);
                }
                Files.createDirectories(destination.getParent());
                if (path.toFile().isDirectory()) {
                    Directories.copy(path, destination);
                } else {
                    Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                }
                git.add().addFilepattern(repositoryRoot.relativize(destination).toString()).call();
            }

            git.commit().setMessage(message).call();
            LOG.infof("Committed to branch %s in repository %s: %s", branch, repositoryRoot, message);
            return true;
        } catch (IOException | GitAPIException | URISyntaxException e) {
            LOG.errorf("Commit failed: %s", e.getMessage());
            return false;
        }
    }

    /**
     * Create a temporary repository, copy the specified paths to a repository and commit them.
     *
     * @param repositoryRoot The root of the repository.
     * @param branch The branch to commit to.
     * @param message The commit message.
     * @param paths The paths to copy.
     *
     * @return The path to the temporary repository.
     */
    public static Optional<Path> commit(String branch, String message, Path... paths) {
        return createTempRepo("origin").flatMap(tempDir -> {
            if (commit(tempDir, branch, message, paths)) {
                return Optional.of(tempDir);
            } else {
                return Optional.empty();
            }
        }).or(() -> Optional.empty());
    }

    /**
     * Create a temporary repository, copy the specified paths to a repository and commit them.
     *
     * @param message The commit message.
     * @param paths The paths
     * @return The path to the temporary repository.
     */
    public static Optional<Path> commit(String message, Path... paths) {
        return commit("main", message, paths);
    }

    /**
     * Open the specified repository and push the specified branch to the remote.
     *
     * @param repositoryRoot The root of the repository.
     * @param remoteName The name of the remote.
     * @param branch The branch to push.
     */
    public static boolean push(Path repositoryRoot, String remoteName, String branch) {
        try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(repositoryRoot.toFile())) {
            PushCommand command = git.push();
            command.setRemote(remoteName);
            command.setForce(true);
            command.setRefSpecs(new RefSpec("refs/heads/" + branch + ":refs/heads/" + branch, WildcardMode.REQUIRE_MATCH));
            command.call();
            LOG.infof("Pushed branch %s to remote %s in repository %s", branch, remoteName, repositoryRoot);
            return true;
        } catch (IOException | GitAPIException e) {
            LOG.errorf("Push failed due to %s", e.getMessage());
            return false;
        }
    }

    /**
     * Open the scm root and push the specified branch to the remote.
     *
     * @param remoteName The name of the remote.
     * @param branch The branch to push.
     */
    public static boolean push(String remoteName, String branch) {
        return getScmRoot().map(root -> {
            return push(root, remoteName, branch);
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

    public static Optional<Path> getScmRoot() {
        Path dir = Paths.get("").toAbsolutePath();
        while (dir != null && !dir.resolve(DOT_GIT).toFile().exists()) {
            dir = dir.getParent();
        }
        return Optional.ofNullable(dir).filter(p -> p.resolve(DOT_GIT).toFile().exists());
    }

    /**
     * Get the git root.
     *
     * @param path Any path under the target git repo.
     * @return The {@link Path} to the git root.
     */
    public static Optional<Path> getRoot(Path path) {
        Path root = path;
        while (root != null && !root.resolve(Git.DOT_GIT).toFile().exists()) {
            root = root.toAbsolutePath().getParent();
        }
        return Optional.ofNullable(root);
    }

    /**
     * Get the git config.
     *
     * @param root the git root.
     * @return The {@link Path} to the git config.
     */
    public static Path getConfig(Path root) {
        return root.resolve(DOT_GIT).resolve(CONFIG);
    }

    public static Path getHead(Path root) {
        return root.resolve(DOT_GIT).resolve(HEAD);
    }

    /**
     * Get the git remote urls as a map.
     *
     * @param path the path to the git config.
     * @return A {@link Map} of urls per remote.
     */
    public static Map<String, String> getRemotes(Path path) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            Iterator<String> linesIter = Files.lines(getConfig(path)).map(String::trim).iterator();
            while (linesIter.hasNext()) {
                remoteValue(linesIter.next()).ifPresent(remote -> {
                    while (linesIter.hasNext()) {
                        String remoteLine = linesIter.next();
                        if (remoteLine.startsWith(URL) && remoteLine.contains(EQUALS)) {
                            result.put(remote, remoteLine.split(EQUALS)[1].trim());
                            break;
                        }
                    }
                });
            }
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * Get the git remote url.
     *
     * @param path the path to the git config.
     * @param remote the remote.
     * @return The an {@link Optional} String with the URL of the specified remote.
     */
    public static Optional<String> getRemoteUrl(Path path, String remote) {
        return getRemoteUrl(path, remote, false);
    }

    public static Optional<String> getSafeRemoteUrl(Path path, String remote) {
        return getRemoteUrl(path, remote, true);
    }

    public static Optional<String> getRemoteUrl(Path path, String remote, boolean httpsPreferred) {
        try {
            Optional<String> url = Files.lines(getConfig(path)).map(String::trim)
                    .filter(inRemote(remote, new AtomicBoolean()))
                    .filter(l -> l.startsWith(URL) && l.contains(EQUALS))
                    .map(s -> s.split(EQUALS)[1].trim())
                    .findAny();
            return httpsPreferred ? url.map(Git::sanitizeRemoteUrl) : url;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String sanitizeRemoteUrl(String remoteUrl) {
        final int atSign = remoteUrl.indexOf('@');
        if (atSign > 0) {
            remoteUrl = remoteUrl.substring(atSign + 1);
            remoteUrl = remoteUrl.replaceFirst(":", "/");
            remoteUrl = "https://" + remoteUrl;
        }
        if (!remoteUrl.endsWith(".git")) {
            remoteUrl += ".git";
        }
        return remoteUrl;
    }

    /**
     * Get the git branch.
     *
     * @param path the path to the git config.
     * @return The an {@link Optional} String with the branch.
     */
    public static Optional<String> getBranch(Path path) {
        try {
            return Files.lines(getHead(path)).map(String::trim)
                    .filter(l -> l.startsWith(REF) && l.contains(SLASH))
                    .map(s -> s.substring(s.lastIndexOf(SLASH) + 1).trim())
                    .findAny();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Get the git branch.
     *
     * @param path the path to the git config.
     * @return The an {@link Optional} String with the branch.
     */
    public static Optional<String> getCommitSHA(Path path) {
        try {
            return Files.lines(getHead(path)).map(String::trim)
                    .filter(l -> l.startsWith(REF) && l.contains(COLN))
                    .map(s -> s.substring(s.lastIndexOf(COLN) + 1).trim())
                    .map(ref -> path.resolve(DOT_GIT).resolve(ref))
                    .filter(ref -> ref.toFile().exists())
                    .map(Git::read)
                    .map(String::trim)
                    .findAny();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Create a predicate function that tracks if the a line is defined in the specified remote section.
     *
     * @param remote The target remote.
     * @param state An atomic boolean which holds the predicate state.
     * @reuturn The predicate.
     */
    public static Predicate<String> inRemote(String remote, AtomicBoolean state) {
        return l -> {
            if (l.startsWith(OB) && l.contains(REMOTE) && l.contains(remote) && l.endsWith(CB)) {
                state.set(true);
            } else if (l.startsWith(OB) && l.endsWith(CB)) {
                state.set(false);
            }
            return state.get();
        };
    }

    public static Optional<String> remoteValue(String line) {
        Pattern p = Pattern.compile(REMOTE_PATTERN);
        Matcher m = p.matcher(line);
        if (m.matches()) {
            return Optional.of(m.group(1));
        } else {
            return Optional.empty();
        }
    }

    private static String read(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
