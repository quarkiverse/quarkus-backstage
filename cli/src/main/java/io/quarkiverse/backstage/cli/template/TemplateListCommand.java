package io.quarkiverse.backstage.cli.template;

import java.util.List;
import java.util.stream.Collectors;

import io.quarkiverse.backstage.cli.common.BackstageClientAwareCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Entity;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Unmatched;

@Command(name = "list", sortOptions = false, mixinStandardHelpOptions = false, header = "Backstage Template List")
public class TemplateListCommand extends BackstageClientAwareCommand {

    public TemplateListCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Unmatched
    List<String> unmatched;

    @Override
    public Integer call() throws Exception {
        List<Entity> result = getBackstageClient().entities().list("kind=template");
        List<TemplateListItem> entityListItems = result.stream().map(TemplateListItem::from)
                .collect(Collectors.toList());

        TemplateListTable table = new TemplateListTable(entityListItems);
        System.out.println(table.getContent());

        return ExitCode.OK;
    }

}
