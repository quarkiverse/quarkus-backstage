package io.quarkiverse.backstage.common.visitors.template;

import java.util.LinkedHashMap;
import java.util.Map;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.Property;
import io.quarkiverse.backstage.scaffolder.v1beta3.PropertyBuilder;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

public class ApplyGiteaParameters extends TypedVisitor<TemplateSpecFluent<?>> {

    private final String repoPropertyKey;
    private final String repositoryHost;

    public ApplyGiteaParameters(String repoPropertyKey, String repositoryHost) {
        this.repoPropertyKey = repoPropertyKey;
        this.repositoryHost = repositoryHost;
    }

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        Map<String, Property> repoProperties = new LinkedHashMap<>();
        repoProperties.put("host", new PropertyBuilder()
                .withName("host")
                .withTitle("Host")
                .withDescription("The host of the git repository")
                .withType("string")
                .withDefaultValue(repositoryHost)
                .withRequired(true)
                .build());

        repoProperties.put("org", new PropertyBuilder()
                .withName("org")
                .withTitle("Organization")
                .withDescription("The organization of the git repository")
                .withType("string")
                .withDefaultValue("dev")
                .withRequired(true)
                .build());

        repoProperties.put("name", new PropertyBuilder()
                .withName("name")
                .withTitle("Name")
                .withDescription("The name of the git repository")
                .withType("string")
                .withDefaultValue("my-app")
                .withRequired(true)
                .build());

        repoProperties.put("branch", new PropertyBuilder()
                .withName("branch")
                .withTitle("Branch")
                .withDescription("The branch of the git repository")
                .withType("string")
                .withDefaultValue("main")
                .withRequired(true)
                .build());

        repoProperties.put("visibility", new PropertyBuilder()
                .withName("visibility")
                .withTitle("Visibility")
                .withDescription("The visibility of the git repository")
                .withType("string")
                .withDefaultValue("public")
                .withRequired(true)
                .build());

        Property repo = new PropertyBuilder()
                .withName("repo")
                .withType("object")
                .withTitle("Repository Configuration")
                .withRequired(true)
                .withProperties(repoProperties)
                .build();

        spec.editMatchingParameter(p -> p.getProperties().containsKey(repoPropertyKey))
                .removeFromProperties(repoPropertyKey)
                .addToProperties(repoPropertyKey, repo)
                .endParameter();
    }
}
