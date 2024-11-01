package io.quarkiverse.backstage.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@SuppressWarnings("unchecked")
public final class Github {

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
