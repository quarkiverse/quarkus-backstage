package io.quarkiverse.backstage.cli.entities;

import java.util.ArrayList;
import java.util.List;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import picocli.CommandLine.Command;

@Command(name = "generate", sortOptions = false, mixinStandardHelpOptions = false, header = "Generate Backstage Entities.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class GenerateCommand extends GenerationBaseCommand {

    public GenerateCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public void process(EntityList entityList) {
        System.out.println("Backstage entities generated:");
        List<EntityListItem> items = new ArrayList<>();
        saveCatalogInfo(entityList);
        for (Entity entity : entityList.getItems()) {
            items.add(EntityListItem.from(entity));
        }
        EntityListTable table = new EntityListTable(items);
        System.out.println(table.getContent());
    }
}
