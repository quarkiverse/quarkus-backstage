package io.quarkiverse.backstage.cli.common;

import java.util.concurrent.Callable;

import io.quarkiverse.backstage.client.BackstageClient;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

public abstract class EntityBaseCommand implements Callable<Integer> {

    BackstageClient backstageClient;

    public EntityBaseCommand(BackstageClient backstageClient) {
        this.backstageClient = backstageClient;
    }

    @Spec
    protected CommandSpec spec;

    @Option(order = 1, names = { "-h", "--help" }, usageHelp = true, description = "Display this help message.")
    public boolean help;

    @Option(order = 2, names = { "--dry-run" }, description = "Show actions that would be taken.")
    boolean dryRun = false;

    public BackstageClient getBackstageClient() {
        return backstageClient;
    }
}
