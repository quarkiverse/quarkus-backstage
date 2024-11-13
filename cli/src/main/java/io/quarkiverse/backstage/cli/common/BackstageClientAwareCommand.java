package io.quarkiverse.backstage.cli.common;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.utils.Projects;
import io.quarkiverse.backstage.common.utils.Serialization;
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

    @Option(order = 1, names = { "-d", "--dev-service" }, description = "Connect to the Backstage Dev Service.")
    public boolean devMode;

    @Option(order = 2, names = { "-h", "--help" }, usageHelp = true, description = "Display this help message.")
    public boolean help;

    @Option(order = 3, names = { "--dry-run" }, description = "Show actions that would be taken.")
    boolean dryRun;

    public BackstageClient getBackstageClient() {
        if (devMode) {
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
        return Arrays.stream(files).sorted((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified())).findFirst()
                .map(f -> {
                    Map<String, String> params = Serialization.unmarshal(f, new TypeReference<Map<String, String>>() {
                    });
                    String url = params.get("url");
                    String token = params.get("token");
                    URI uri = URI.create(url);
                    return new BackstageClient(uri.getHost(), uri.getPort(), token);
                });
    }
}
