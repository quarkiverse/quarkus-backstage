package io.quarkiverse.backstage.common.visitors.template;

import java.util.LinkedHashMap;
import java.util.Map;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.Property;
import io.quarkiverse.backstage.scaffolder.v1beta3.PropertyBuilder;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

public class ApplyGiteaParameters extends TypedVisitor<TemplateSpecFluent<?>> {

    private final String repositoryHost;

    public ApplyGiteaParameters(String repositoryHost) {
        this.repositoryHost = repositoryHost;
    }

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        Map<String, Property> repoProperties = new LinkedHashMap<>();
        repoProperties.put("repoHost", new PropertyBuilder()
                .withName("repoHost")
                .withTitle("Host")
                .withDescription("The host of the git repository")
                .withType("string")
                .withDefaultValue(repositoryHost)
                .withRequired(true)
                .build());

        repoProperties.put("repoOrg", new PropertyBuilder()
                .withName("repoOrg")
                .withTitle("Organization")
                .withDescription("The organization of the git repository")
                .withType("string")
                .withDefaultValue("dev")
                .withRequired(true)
                .build());

        repoProperties.put("repoName", new PropertyBuilder()
                .withName("repoName")
                .withTitle("Name")
                .withDescription("The name of the git repository")
                .withType("string")
                .withDefaultValue("my-app")
                .withRequired(true)
                .build());

        repoProperties.put("repoBranch", new PropertyBuilder()
                .withName("repoBranch")
                .withTitle("Branch")
                .withDescription("The branch of the git repository")
                .withType("string")
                .withDefaultValue("main")
                .withRequired(true)
                .build());

        repoProperties.put("repoVisibility", new PropertyBuilder()
                .withName("repoVisibility")
                .withTitle("Visibility")
                .withDescription("The visibility of the git repository")
                .withType("string")
                .withDefaultValue("public")
                .withRequired(true)
                .build());

        repoProperties.forEach((repoPropertyKey, repoProperty) -> {
            spec.editMatchingParameter(p -> p.getProperties().containsKey(repoPropertyKey))
                    .removeFromProperties(repoProperties)
                    .addToProperties(repoProperties)
                    .endParameter();
        });
    }
}
