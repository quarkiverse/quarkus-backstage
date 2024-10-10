package io.quarkiverse.backstage.cli.entities;

import java.util.List;
import java.util.stream.Collectors;

import io.quarkiverse.backstage.cli.common.EntityBaseCommand;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Entity;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Unmatched;

@Command(name = "list", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Entity List")
public class ListCommand extends EntityBaseCommand {

    public ListCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Unmatched
    List<String> unmatched;

    @Override
    public Integer call() throws Exception {
        List<Entity> entities = getBackstageClient().getAllEntities();
        List<EntityListItem> entityListItems = entities.stream().map(EntityListItem::from).collect(Collectors.toList());

        EntityListTable table = new EntityListTable(entityListItems);
        System.out.println(table.getContent());

        return ExitCode.OK;
    }

}
