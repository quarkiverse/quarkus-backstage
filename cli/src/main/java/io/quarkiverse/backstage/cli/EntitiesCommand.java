package io.quarkiverse.backstage.cli;

import java.util.concurrent.Callable;

import io.quarkiverse.backstage.cli.entities.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "entities", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Entities", subcommands = {
        EntityListCommand.class, EntityInstallCommand.class, EntityUninstallCommand.class, EntityGenerateCommand.class })
public class EntitiesCommand implements Callable<Integer> {

    @Spec
    protected CommandSpec spec;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Display this help message.")
    public boolean help;

    @Override
    public Integer call() {
        CommandLine schemaCommand = spec.subcommands().get("list");
        return schemaCommand.execute();
    }
}
