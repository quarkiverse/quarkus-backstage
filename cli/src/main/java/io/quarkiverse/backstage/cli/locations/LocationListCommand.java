package io.quarkiverse.backstage.cli.locations;

import java.util.List;
import java.util.stream.Collectors;

import io.quarkiverse.backstage.cli.common.BackstageClientAwareCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.Location;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Unmatched;

@Command(name = "list", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Locations List")
public class LocationListCommand extends BackstageClientAwareCommand {

    public LocationListCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Unmatched
    List<String> unmatched;

    @Override
    public Integer call() throws Exception {
        List<Entity> result = getBackstageClient().entities().list("kind=location");
        List<LocationListItem> locationListItems = result.stream()
                .filter(e -> e instanceof Location)
                .map(e -> (Location) e)
                .map(LocationListItem::from).collect(Collectors.toList());

        LocationListTable table = new LocationListTable(locationListItems);
        System.out.println(table.getContent());

        return ExitCode.OK;
    }

}
