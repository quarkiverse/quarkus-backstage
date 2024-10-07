package io.quarkiverse.backstage.cli.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.microprofile.config.ConfigProvider;
import org.kohsuke.github.GHGist;
import org.kohsuke.github.GHGistBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@SuppressWarnings("unchecked")
public final class Github {

    private static String token;

    public static void useToken(String token) {
        Github.token = token;
    }

    public static Optional<String> getToken() {
        return Optional.ofNullable(token)
                .or(() -> ConfigProvider.getConfig().getOptionalValue("quarkus.backstage.github.token", String.class))
                .or(() -> getGHToken());
    }

    public static Optional<String> createPrivateGist(Optional<String> githubToken, String description, String fileName,
            String content) {
        try {
            String token = githubToken.orElseThrow();
            var github = org.kohsuke.github.GitHub.connectUsingOAuth(token);
            GHGistBuilder gistBuilder = github.createGist()
                    .description(description)
                    .public_(false) // This makes the gist private
                    .file(fileName, content);

            return Optional.of(gistBuilder.create().getFile(fileName).getRawUrl().toString());
        } catch (IOException e) {
            System.err.println("Failed to create gist.");
            return Optional.empty();
        }
    }

    public static Optional<String> createOrUpdatePrivateGist(Optional<String> githubToken, String description, String fileName,
            String content) {
        List<String> existingGists = findAllGists(githubToken, description, fileName);
        if (existingGists.isEmpty()) {
            return createPrivateGist(githubToken, description, fileName, content);
        } else {
            String gistId = existingGists.get(0);
            return updateGistFile(githubToken, gistId, fileName, content);
        }
    }

    public static List<String> findAllGists(Optional<String> githubToken, String description, String fileName) {
        List<String> result = new ArrayList<>();
        try {
            String token = githubToken.orElseThrow();
            var github = org.kohsuke.github.GitHub.connectUsingOAuth(token);
            for (GHGist gist : github.getMyself().listGists()) {
                if (gist.getDescription().equals(description) && gist.getFiles().containsKey(fileName)) {
                    result.add(gist.getGistId());
                }
            }
            return result;
        } catch (IOException e) {
            return result;
        }
    }

    private static Optional<String> updateGistFile(Optional<String> githubToken, String gistId, String fileName,
            String content) {
        try {
            String token = githubToken.orElseThrow();
            var github = org.kohsuke.github.GitHub.connectUsingOAuth(token);
            var gist = github.getGist(gistId);
            return Optional.of(gist.update().updateFile(fileName, content).update().getFile(fileName).getRawUrl().toString());
        } catch (IOException e) {
            System.err.println("Failed to update gist:" + gistId);
            return Optional.empty();
        }
    }

    private static Optional<Path> getGHHostsPath() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            // For Windows, use the APPDATA directory
            String appData = System.getenv("APPDATA");
            if (appData == null) {
                System.err.println("APPDATA environment variable not found.");
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
                System.err.println("GitHub token not found in hosts.yml");
                return Optional.empty();
            }
        } catch (IOException e) {
            System.err.println("Error reading GitHub tokne hosts.yml");
            return Optional.empty();
        }
    }
}
