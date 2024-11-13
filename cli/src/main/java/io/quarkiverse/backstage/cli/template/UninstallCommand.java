package io.quarkiverse.backstage.cli.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.ws.rs.WebApplicationException;

import io.quarkiverse.backstage.cli.common.GenerationBaseCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.common.handlers.GetBackstageTemplatesHandler;
import io.quarkiverse.backstage.spi.DevTemplateBuildItem;
import io.quarkiverse.backstage.spi.TemplateBuildItem;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import picocli.CommandLine.Command;

@Command(name = "uninstall", sortOptions = false, mixinStandardHelpOptions = false, header = "Uninstall Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class UninstallCommand extends GenerationBaseCommand<List<TemplateBuildItem>> {

    public UninstallCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public String getHandlerName() {
        return GetBackstageTemplatesHandler.class.getName();
    }

    @Override
    public String[] getRequiredBuildItems() {
        return new String[] { TemplateBuildItem.class.getName(), DevTemplateBuildItem.class.getName() };
    }

    @Override
    public void process(List<TemplateBuildItem> templateBuildItems) {
        QuarkusProject project = QuarkusProjectHelper.getProject(getWorkingDirectory());
        List<TemplateListItem> items = new ArrayList<>();

        for (TemplateBuildItem templateBuildItem : templateBuildItems) {
            String templateName = templateBuildItem.getTemplate().getMetadata().getName();

            List<Entity> result = getBackstageClient().entities().list("kind=template,metadata.name=" + templateName);
            Map<String, String> locationIdByTarget = new HashMap<>();

            List<LocationEntry> locations = getBackstageClient().locations().list();
            for (LocationEntry item : locations) {
                locationIdByTarget.put(item.getTarget(), item.getId());
            }

            boolean templateFound = false;
            boolean locationFound = false;
            for (Entity entity : result) {
                Entity refreshed = null;
                try {
                    refreshed = getBackstageClient().entities()
                            .withKind(entity.getKind().toLowerCase())
                            .withName(entity.getMetadata().getName())
                            .inNamespace(entity.getMetadata().getNamespace().orElse("default"))
                            .get();
                    String locationTarget = refreshed.getMetadata().getAnnotations()
                            .get("backstage.io/managed-by-origin-location")
                            .replaceAll("url:", "")
                            .replaceAll("file:", "");

                    templateFound = true;
                    if (locationIdByTarget.containsKey(locationTarget)) {
                        locationFound = true;
                        String locationId = locationIdByTarget.get(locationTarget);
                        getBackstageClient().locations().withId(locationId).delete();
                        Optional<String> uuid = refreshed.getMetadata().getUid();
                        uuid.ifPresent(u -> getBackstageClient().entities().withUID(u).delete());
                        items.add(TemplateListItem.from(entity));
                    }
                } catch (WebApplicationException e) {
                    if (e.getResponse().getStatus() == 404) {
                        System.out.println(entity.getKind() + " " + entity.getMetadata().getName() + " not installed.");
                    } else {
                        throw new RuntimeException(e);
                    }
                }

                if (!templateFound) {
                    System.out.println("No matching Template named: " + templateName);
                } else if (!locationFound) {
                    System.out.println(
                            "No matching Location found for Template: " + templateName + ". Template cannot be uninstalled.");
                } else if (!items.isEmpty()) {
                    System.out.println("Uninstalled Backstage templates:");
                    TemplateListTable table = new TemplateListTable(items);
                    System.out.println(table.getContent());
                } else {
                    System.out.println("Did not uninstall any Backstage templates.");
                }
            }
        }
    }
}
