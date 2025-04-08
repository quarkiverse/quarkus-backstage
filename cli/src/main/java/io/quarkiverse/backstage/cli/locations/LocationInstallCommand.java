package io.quarkiverse.backstage.cli.locations;

import io.quarkiverse.backstage.cli.common.BackstageClientAwareCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.CreateLocationResponse;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;

@Command(name = "install", sortOptions = false, mixinStandardHelpOptions = false, header = "Install Location.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class LocationInstallCommand extends BackstageClientAwareCommand {

    @Parameters(index = "0", arity = "1..1", description = "The location target.")
    private String target;

    public LocationInstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public Integer call() throws Exception {
        CreateLocationResponse response;
        if (target.matches("^(http|https|git)://.*$")) {
            response = getBackstageClient().locations().createFromUrl(target);
        } else {
            response = getBackstageClient().locations().createFromFile(target);
        }

        System.out.println("Created location:" + response.getLocation().getId());
        response.getEntities().forEach(e -> System.out.println(" - " + e.getKind() + ":" + e.getMetadata().getName()));

        return ExitCode.OK;
    }
}
