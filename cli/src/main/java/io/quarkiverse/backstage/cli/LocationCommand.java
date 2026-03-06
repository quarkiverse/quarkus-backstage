package io.quarkiverse.backstage.cli;

import java.util.concurrent.Callable;

import io.quarkiverse.backstage.cli.locations.LocationInstallCommand;
import io.quarkiverse.backstage.cli.locations.LocationListCommand;
import io.quarkiverse.backstage.cli.locations.LocationUninstallCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "location", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Location", subcommands = {
        LocationListCommand.class, LocationInstallCommand.class, LocationUninstallCommand.class })
public class LocationCommand implements Callable<Integer> {

    @Spec
    protected CommandSpec spec;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Display this help message.")
    public boolean help;

    @Override
    public Integer call() {
        CommandLine listCommand = spec.subcommands().get("list");
        return listCommand.execute();
    }
}
