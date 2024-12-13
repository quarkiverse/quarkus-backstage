package io.quarkiverse.backstage.cli.entities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.handlers.GetBackstageEntitiesHandler;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import picocli.CommandLine.Command;

@Command(name = "generate", sortOptions = false, mixinStandardHelpOptions = false, header = "Generate Backstage Entities.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class GenerateCommand extends GenerationBaseCommand<EntityList> {

    public GenerateCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public void process(EntityList entityList, Path... additionalFiles) {
        System.out.println("Backstage entities generated:");
        List<EntityListItem> items = new ArrayList<>();
        saveCatalogInfo(entityList);
        for (Entity entity : entityList.getItems()) {
            items.add(EntityListItem.from(entity));
        }
        EntityListTable table = new EntityListTable(items);
        System.out.println(table.getContent());
    }

    @Override
    public String getHandlerName() {
        return GetBackstageEntitiesHandler.class.getName();
    }

    @Override
    public String[] getRequiredBuildItems() {
        return new String[] { EntityListBuildItem.class.getName() };
    }
}
