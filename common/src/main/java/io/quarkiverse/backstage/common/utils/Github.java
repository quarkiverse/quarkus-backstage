package io.quarkiverse.backstage.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@SuppressWarnings("unchecked")
public final class Github {
    private static final Pattern GITHUB_URL_PATTERN = Pattern.compile(
            "^(https?://)?(www|raw\\.)?(github|githubusercontent)\\.com/.*",
            Pattern.CASE_INSENSITIVE);

    private static final Logger LOG = Logger.getLogger(Github.class);
    private static String token;

    public static void useToken(String token) {
        Github.token = token;
    }

    public static Optional<String> getToken() {
        return Optional.ofNullable(token)
                .or(() -> ConfigProvider.getConfig().getOptionalValue("quarkus.backstage.github.token", String.class))
                .or(() -> getGHToken());
    }

    public static boolean isGithubUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        try {
            URL u = URI.create(url).toURL();
            if (u.getHost().contains("github.com") || u.getHost().contains("githubusercontent.com")) {
                return true;
            }
            return false;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format.", e);
        }
    }

    public static String toHttpCloneUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        try {
            URL parsedUrl = URI.create(url).toURL();
            String host = parsedUrl.getHost();

            if (!host.equalsIgnoreCase("github.com")) {
                throw new IllegalArgumentException("Provided URL is not a GitHub URL.");
            }

            // Remove query parameters and fragments
            String path = parsedUrl.getPath().replaceAll("/$", ""); // Remove trailing slash
            if (path.endsWith(".git")) {
                return "https://" + host + path;
            } else {
                return "https://" + host + path + ".git";
            }
        } catch (MalformedURLException e) {
            // Handle cases like raw Git URLs
            if (url.startsWith("git@github.com:")) {
                return url.replace("git@github.com:", "https://github.com/");
            }
            throw new IllegalArgumentException("Invalid URL format.", e);
        }
    }

    public static Optional<String> findGithubOwner(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        if (!isGithubUrl(url)) {
            throw new IllegalArgumentException("Provided URL is not a valid GitHub URL.");
        }

        try {
            URL parsedUrl = URI.create(url).toURL();
            String path = parsedUrl.getPath();
            String[] segments = path.split("/");

            if (segments.length < 2) {
                throw new IllegalArgumentException(
                        "URL does not contain enough information to determine the user or organization.");
            }

            return Optional.of(segments[1]);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format.", e);
        }
    }

    public static Optional<String> extractGithubRepositoryName(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        if (!isGithubUrl(url)) {
            throw new IllegalArgumentException("Provided URL is not a valid GitHub URL.");
        }

        try {
            URL parsedUrl = URI.create(url).toURL();
            String path = parsedUrl.getPath();
            String[] segments = path.split("/");

            if (segments.length < 3) {
                throw new IllegalArgumentException("URL does not contain enough information to determine the repository name.");
            }

            return Optional.of(segments[2]).map(s -> s.replaceAll("\\.git$", ""));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format.", e);
        }
    }

    public static String toSshCloneUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        try {
            URL parsedUrl = URI.create(url).toURL();
            String host = parsedUrl.getHost();
            String owner = findGithubOwner(url)
                    .orElseThrow(() -> new IllegalArgumentException("Could not determine the owner of the repository."));
            String repositoryName = extractGithubRepositoryName(url)
                    .orElseThrow(() -> new IllegalArgumentException("Could not determine the repository name."));
            return String.format("git@github.com:%s/%s.git", owner, repositoryName);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format.", e);
        }
    }

    public static Path toRelativePath(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        try {
            URL parsedUrl = URI.create(url).toURL();
            String host = parsedUrl.getHost();

            // Validate that the URL is from GitHub
            if (!isGithubUrl(url)) {
                throw new IllegalArgumentException("Provided URL is not a valid GitHub URL.");
            }

            // Extract the path part of the URL
            String path = parsedUrl.getPath();
            String[] segments = path.split("/");

            // GitHub file URLs should have at least 5 segments: /user/repo/branch/path/to/file
            if (segments.length < 2) {
                throw new IllegalArgumentException("URL does not contain enough information to determine the file path.");
            } else if (segments.length < 5) {
                return Paths.get(".");
            }

            if (segments[3].equals("blob")) {
                return Paths.get(String.join("/", segments).substring(path.indexOf(segments[5])));
            } else if (segments[3].equals("refs") && segments[4].equals("heads")) {
                return Paths.get(String.join("/", segments).substring(path.indexOf(segments[6])));
            }

            return Paths.get(".");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format.", e);
        }
    }

    private static Optional<Path> getGHHostsPath() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            // For Windows, use the APPDATA directory
            String appData = System.getenv("APPDATA");
            if (appData == null) {
                LOG.debug("APPDATA environment variable not found.");
                return Optional.empty();
            }
            return Optional.of(Paths.get(appData, "GitHub CLI", "hosts.yml").toAbsolutePath());
        } else {
            // For Linux/Mac, use XDG_CONFIG_HOME or fallback to ~/.config
            String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            if (xdgConfigHome == null || xdgConfigHome.isEmpty()) {
                xdgConfigHome = Paths.get(System.getProperty("user.home"), ".config").toAbsolutePath().toString();
            }
            return Optional.of(Paths.get(xdgConfigHome, "gh", "hosts.yml").toAbsolutePath());
        }
    }

    private static Optional<String> getGHToken() {
        return getGHHostsPath().map(Path::toFile).filter(File::exists).flatMap(Github::getGHToken);
    }

    private static Optional<String> getGHToken(File hostsFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Map<String, Object> data = mapper.readValue(hostsFile, Map.class);
            Map<String, Object> githubData = (Map<String, Object>) data.get("github.com");
            if (githubData != null && githubData.containsKey("oauth_token")) {
                String token = (String) githubData.get("oauth_token");
                return Optional.ofNullable(token);
            } else {
                LOG.debug("GitHub token not found in hosts.yml");
                return Optional.empty();
            }
        } catch (IOException e) {
            LOG.debug("Error reading GitHub tokne hosts.yml");
            return Optional.empty();
        }
    }
}
