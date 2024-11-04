package io.quarkiverse.backstage.common.dsl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.quarkiverse.backstage.common.utils.Git;
import io.quarkus.jgit.deployment.GiteaDevServiceInfoBuildItem;

public class Gitea {

    private static final String DEFAULT_ORGANIZATION = "dev";
    private static final String DEFAULT_REPOSITORY = "my-app";
    private static final Path DEFAULT_PATH = Paths.get(System.getProperty("user.dir"));
    private static final Pattern URL_PATTERN = Pattern.compile("(http[s]?)://([^:/]+)(?::([0-9]+))?/?(.*)");

    private String host;
    private int port;

    private String sharedHost;
    private int sharedPort;

    private String username;
    private String password;

    private String organization = "dev";
    private String repository;
    private String remote = "origin";
    private String branch = "backstage";

    private Path projectRootDir;

    private Gitea(GiteaDevServiceInfoBuildItem giteaDevServiceInfo) {
        this(giteaDevServiceInfo.host(), giteaDevServiceInfo.httpPort(),
                giteaDevServiceInfo.sharedNetworkHost().orElse("gitea"),
                giteaDevServiceInfo.sharedNetworkHttpPort().orElse(3000),
                giteaDevServiceInfo.adminUsername(), giteaDevServiceInfo.adminPassword(),
                DEFAULT_ORGANIZATION, DEFAULT_REPOSITORY, DEFAULT_PATH);
    }

    private Gitea(String host, int port, String sharedHost, int sharedPort, String username, String password,
            String organization, String repository, Path projectRootDir) {
        this.host = host;
        this.port = port;
        this.sharedHost = sharedHost;
        this.sharedPort = sharedPort;
        this.username = username;
        this.password = password;
        this.organization = organization != null ? organization : "dev";
        this.repository = repository;
        this.projectRootDir = projectRootDir;
    }

    static String getHost(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        throw new IllegalArgumentException("Invalid URL: " + url);
    }

    static int getPort(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(3));
        }
        throw new IllegalArgumentException("Invalid URL: " + url);
    }

    public static Gitea create(GiteaDevServiceInfoBuildItem giteaDevServiceInfo) {
        return new Gitea(giteaDevServiceInfo);
    }

    public static Gitea create(String url, String username, String password, String organization, String repository,
            Path projectRootDir) {
        return new Gitea(getHost(url), getPort(url), "gitea", 3000, username,
                password, organization, repository, projectRootDir);
    }

    public static Gitea create(String host, int port, String username, String password, String organization, String repository,
            Path projectRootDir) {
        return new Gitea(host, port, "gitea", 3000, username, password, organization, repository, projectRootDir);
    }

    public Gitea withOrganization(String organization) {
        return new Gitea(host, port, sharedHost, sharedPort, username, password, organization, repository, projectRootDir);
    }

    public Gitea withRepository(String repository) {
        return new Gitea(host, port, sharedHost, sharedPort, username, password, organization, repository, projectRootDir);
    }

    public boolean pushProject(Path projectRootDir) {
        return pushProject(projectRootDir, repository);
    }

    public boolean pushProject(Path projectRootDir, String name) {
        return new Gitea(host, port, sharedHost, sharedPort, username, password, organization, name, projectRootDir)
                .commitAndPush();
    }

    public void withSharedReference(Path path, Consumer<String> consumer) {
        String url = Git.getUrlFromBase(getRepositorySharedUrl(), branch, path)
                .orElseThrow(() -> new IllegalStateException("Cannot create shared reference for:" + path));
        consumer.accept(url);
    }

    private boolean commitAndPush() {
        String remoteUrl = getRepositoryUrl();
        if (remoteUrl != null) {
            GitActions.createTempo()
                    .addRemote(remote, remoteUrl)
                    .createBranch(branch)
                    .importFiles(projectRootDir)
                    .commit("Generated backstage resources.")
                    .push(remote, branch, username, password);
            return true;
        }

        GitActions.createTempo()
                .checkoutOrCreateBranch(remote, branch)
                .importFiles(projectRootDir)
                .commit("Generated backstage resources.")
                .push(remote, branch);

        return true;
    }

    /**
     * Create a URL to the repository that is accessible by the runtime.
     *
     * @return the URL to the repository
     */
    private String getRepositoryUrl() {
        return "http://" + host + ":" + port + "/" + organization + "/" + repository;
    }

    /**
     * Create a URL to the repository that is accessible by the shared network (container to container).
     *
     * @return the URL to the repository
     */
    private String getRepositorySharedUrl() {
        return "http://" + sharedHost + ":" + sharedPort + "/" + organization + "/" + repository;
    }
}
