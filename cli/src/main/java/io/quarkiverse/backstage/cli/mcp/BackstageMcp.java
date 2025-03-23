package io.quarkiverse.backstage.cli.mcp;

import static io.quarkiverse.backstage.cli.common.Properties.extractValues;
import static io.quarkiverse.backstage.common.utils.Maps.flattenOptionals;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.scaffolder.v1beta3.Parameter;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;

@ApplicationScoped
public class BackstageMcp {

    @Inject
    BackstageClient client;

    @Tool(description = "List all Backstage entity kinds")
    public List<String> listKinds() {
        return List.of("location", "component", "api", "template", "user", "group");
    }

    @Tool(description = "List backstage entities by filtered by kind. If no kind is specified, all entities are listed")
    public List<String> list(@ToolArg(description = "Backstage entity kind") Optional<String> kind) {
        try {
            return client.entities().list(kind.map(k -> "kind=" + k.toLowerCase()).orElse(null)).stream()
                    .map(e -> e.getMetadata().getName()).toList();
        } catch (Exception e) {
            return List.of(e.getMessage());
        }
    }

    @Tool(description = "Get the Backstage entity in YAML format")
    public String getEntityYaml(@ToolArg(description = "Entity kind") String kind,
            @ToolArg(description = "Entity name") String name) {
        Entity entity = client.entities().withKind(kind).withName(name).inNamespace("default").get();
        return Serialization.asYaml(entity);
    }

    @Tool
    public String getTemplateDefaultParametersInYaml(@ToolArg(description = "Template name") String templateName,
            @ToolArg(description = "Template namespace") Optional<String> templateNamespace) {
        var template = client.templates().withName(templateName).inNamespace(templateNamespace.orElse("default")).get();
        Map<String, Object> defaultValues = new HashMap<>();
        String json = "";
        for (Parameter parameter : template.getSpec().getParameters()) {
            defaultValues.putAll(extractValues(parameter.getProperties()));
            json += Serialization.asJson(flattenOptionals(defaultValues));
        }
        return json;
    }

    @Tool(description = "Create a backstage project using a template and return the location id")
    public String createProject(@ToolArg(description = "Template name") String templateName,
            @ToolArg(description = "Template namespace") Optional<String> templateNamespace,
            @ToolArg(description = "Path to template parameters yaml file") Optional<String> valuesFile) {

        Map<String, Object> values = valuesFile.map(Paths::get)
                .map(Path::toFile)
                .filter(File::exists)
                .map(f -> Serialization.unmarshal(f, new TypeReference<Map<String, Object>>() {
                }))
                .orElseGet(() -> Serialization.unmarshal(getTemplateDefaultParametersInYaml(templateName, templateNamespace),
                        new TypeReference<Map<String, Object>>() {
                        }));
        return client.templates().withName(templateName).inNamespace(templateNamespace.orElse("default")).instantiate(values);
    }

}
