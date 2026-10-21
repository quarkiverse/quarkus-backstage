package io.quarkiverse.backstage.cli.locations;

import java.util.Optional;

import io.quarkiverse.backstage.cli.common.BackstageClientAwareCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.runtime.BackstageInstallService;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;

@Command(name = "uninstall", sortOptions = false, mixinStandardHelpOptions = false, header = "Uninstall Location.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class LocationUninstallCommand extends BackstageClientAwareCommand {

    @Parameters(index = "0", arity = "1..1", description = "The location target.")
    private String target;

    public LocationUninstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public Integer call() throws Exception {
        BackstageInstallService installService = getInstallService();
        Optional<String> id = installService.findLocationId(target);
        if (id.isEmpty()) {
            System.out.println("Could not find a location with target: " + target);
            return ExitCode.USAGE;
        }

        String locationId = id.get();
        if (installService.uninstallLocation(locationId)) {
            System.out.println("Location uninstalled: " + locationId);
            return ExitCode.OK;
        } else {
            System.out.println("Failed to uninstall location: " + locationId + " for target:" + target);
            return ExitCode.SOFTWARE;
        }
    }
}
