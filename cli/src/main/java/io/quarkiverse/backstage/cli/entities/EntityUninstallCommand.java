package io.quarkiverse.backstage.cli.entities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.handlers.GetBackstageEntitiesHandler;
import io.quarkiverse.backstage.runtime.BackstageInstallService.UninstallStatus;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "uninstall", sortOptions = false, mixinStandardHelpOptions = false, header = "Uninstall Backstage Application.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class EntityUninstallCommand extends GenerationBaseCommand<EntityList> {

    @Parameters(index = "0", arity = "0..1", description = "The name of the template.")
    private Optional<String> name = Optional.empty();

    public EntityUninstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public String getHandlerName() {
        return GetBackstageEntitiesHandler.class.getName();
    }

    @Override
    public String[] getRequiredBuildItems() {
        return new String[] { EntityListBuildItem.class.getName() };
    }

    @Override
    public void process(EntityList entityList, Path... additionalFiles) {
        if (entityList.getItems().isEmpty()) {
            System.out.println("No Backstage entities detected.");
            return;
        }
        List<EntityListItem> items = new ArrayList<>();

        List<Entity> toDelete = new ArrayList<>();
        name.ifPresentOrElse(name -> {
            StringBuilder sb = new StringBuilder();
            sb.append("metadata.name=").append(name);
            namespace.ifPresent(namespace -> sb.append(",metadata.namespace=").append(namespace));
            String filter = sb.toString();
            getBackstageClient().entities().list(filter).forEach(toDelete::add);
        }, () -> toDelete.addAll(entityList.getItems()));

        System.out.println();
        getInstallService().uninstallEntities(toDelete).forEach((entity, status) -> {
            if (status == UninstallStatus.UNINSTALLED) {
                items.add(EntityListItem.from(entity));
            } else if (status == UninstallStatus.NOT_INSTALLED) {
                System.out.println(entity.getKind() + " " + entity.getMetadata().getName() + " not installed.");
            }
        });

        if (!items.isEmpty()) {
            System.out.println("Uninstalled Backstage entities:");
            EntityListTable table = new EntityListTable(items);
            System.out.println(table.getContent());
        } else {
            System.out.println("Did not uninstall any Backstage entities.");
        }
    }
}
