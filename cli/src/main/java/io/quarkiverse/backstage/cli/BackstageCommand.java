package io.quarkiverse.backstage.cli;

import java.util.concurrent.Callable;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@TopCommand
@Command(name = "backstage", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage CLI", subcommands = {
        EntitiesCommand.class })
public class BackstageCommand implements Callable<Integer> {

    @Spec
    protected CommandSpec spec;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Display this help message.")
    public boolean help;

    @Override
    public Integer call() {
        CommandLine entitiesCommand = spec.subcommands().get("entities");
        return entitiesCommand.execute();
    }

}
