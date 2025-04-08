package io.quarkiverse.backstage.cli;

import java.util.concurrent.Callable;

import io.quarkiverse.backstage.cli.template.TemplateGenerateCommand;
import io.quarkiverse.backstage.cli.template.TemplateInfoCommand;
import io.quarkiverse.backstage.cli.template.TemplateInstallCommand;
import io.quarkiverse.backstage.cli.template.TemplateInstantiateCommand;
import io.quarkiverse.backstage.cli.template.TemplateListCommand;
import io.quarkiverse.backstage.cli.template.TemplateUninstallCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "template", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Template", subcommands = {
        TemplateListCommand.class, TemplateInstallCommand.class, TemplateUninstallCommand.class, TemplateGenerateCommand.class,
        TemplateInstantiateCommand.class,
        TemplateInfoCommand.class })
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
