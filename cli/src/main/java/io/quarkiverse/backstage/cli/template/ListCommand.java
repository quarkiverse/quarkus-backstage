package io.quarkiverse.backstage.cli.template;

import java.util.List;
import java.util.stream.Collectors;

import io.quarkiverse.backstage.cli.common.EntityBaseCommand;
import io.quarkiverse.backstage.rest.EntityQueryResult;
import io.quarkiverse.backstage.runtime.BackstageClient;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Unmatched;

@Command(name = "list", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Template List")
public class ListCommand extends EntityBaseCommand {

    public ListCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Unmatched
    List<String> unmatched;

    @Override
    public Integer call() throws Exception {
        EntityQueryResult result = getBackstageClient().getEntities("kind=template");
        List<TemplateListItem> entityListItems = result.getItems().stream().map(TemplateListItem::from)
                .collect(Collectors.toList());

        TemplateListTable table = new TemplateListTable(entityListItems);
        System.out.println(table.getContent());

        return ExitCode.OK;
    }

}
