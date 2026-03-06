package io.quarkiverse.backstage.cli.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.utils.DevServices;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Strings;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

public abstract class BackstageClientAwareCommand implements Callable<Integer> {

    BackstageClient backstageClient;

    public BackstageClientAwareCommand(BackstageClient backstageClient) {
        this.backstageClient = backstageClient;
    }

    @Spec
    protected CommandSpec spec;

    @Option(order = 1, names = { "-h", "--help" }, usageHelp = true, description = "Display this help message.")
    public boolean help;

    @Option(order = 2, names = { "--dry-run" }, description = "Show actions that would be taken.")
    boolean dryRun;

    public BackstageClient getBackstageClient() {
        if (!isBackstageClientConfigured() && DevServices.hasDevServicesConfiguration("backstage")) {
            System.out.println("Using Backstage Dev Service");
            return getDevModeClient().orElseThrow(() -> new IllegalStateException("No Backstage Dev Service found."));
        }
        return backstageClient;
    }

    private Optional<BackstageClient> getDevModeClient() {
        Path projectRootDir = Projects.getProjectRoot();
        Path backstageDevDir = projectRootDir.resolve(".quarkus").resolve("dev").resolve("backstage");
        File[] files = backstageDevDir.toFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return !f.isDirectory() && f.getName().endsWith(".yaml");
            }
        });

        // Let's clean up any stale files
        DevServices.cleanupDevServicesConfiguration("backstage");
        Map<String, String> devServiceConfig = DevServices.getDevServicesConfiguration("backstage");
        try {
            String url = devServiceConfig.get("url");
            String token = devServiceConfig.get("token");
            if (Strings.isNullOrEmpty(url) || Strings.isNullOrEmpty(token)) {
                return Optional.empty();
            }
            URI uri = URI.create(url);
            return Optional.of(new BackstageClient(uri.getHost(), uri.getPort(), token));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Backstage Dev Service configuration", e);
        }
    }

    private boolean isBackstageClientConfigured() {
        Optional<String> url = getConfigValue("quarkus.backstage.url");
        Optional<String> token = getConfigValue("quarkus.backstage.token");
        return url.isPresent() && token.isPresent();
    }

    private Optional<String> getConfigValue(String key) {
        Path moduleRoot = Projects.getModuleRoot(Paths.get(System.getProperty("user.dir")));
        Path resourcesPath = moduleRoot.resolve("src").resolve("main").resolve("resources");
        Path applicationPropertiesPath = resourcesPath.resolve("application.properties");
        Path applicationYamlPath = resourcesPath.resolve("application.yaml");

        if (System.getProperty(key) != null) {
            return Optional.of(System.getProperty(key));
        }

        String envVarName = key.toUpperCase().replace(".", "_");
        if (System.getenv(envVarName) != null) {
            return Optional.of(System.getenv(envVarName));
        }

        if (applicationPropertiesPath.toFile().exists()) {
            Properties properties = new Properties();
            try (InputStream is = new FileInputStream(applicationPropertiesPath.toFile())) {
                properties.load(is);
                return Optional.ofNullable(properties.getProperty(key));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read application.properties", e);
            }
        }
        if (applicationYamlPath.toFile().exists()) {
            Map<String, Object> map = Serialization.unmarshal(applicationYamlPath.toFile(),
                    new TypeReference<Map<String, Object>>() {
                    });
            return Optional.ofNullable((String) map.get(key));
        }
        return Optional.empty();
    }
}
