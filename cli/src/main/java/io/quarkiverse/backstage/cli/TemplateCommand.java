package io.quarkiverse.backstage.cli;

import java.util.concurrent.Callable;

import io.quarkiverse.backstage.cli.template.GenerateCommand;
import io.quarkiverse.backstage.cli.template.InstallCommand;
import io.quarkiverse.backstage.cli.template.InstantiateCommand;
import io.quarkiverse.backstage.cli.template.ListCommand;
import io.quarkiverse.backstage.cli.template.UninstallCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "template", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Template", subcommands = {
        ListCommand.class, InstallCommand.class, UninstallCommand.class, GenerateCommand.class, InstantiateCommand.class })
public class TemplateCommand implements Callable<Integer> {

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
