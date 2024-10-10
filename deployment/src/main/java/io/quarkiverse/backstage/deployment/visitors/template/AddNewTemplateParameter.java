package io.quarkiverse.backstage.deployment.visitors.template;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.ParameterFluent;
import io.quarkiverse.backstage.scaffolder.v1beta3.Property;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

public class AddNewTemplateParameter extends TypedVisitor<TemplateSpecFluent<?>> {

    private final String title;
    private final Property[] properties;

    public AddNewTemplateParameter(String title, Property... properties) {
        this.title = title;
        this.properties = properties;
    }

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        spec.addNewParameter()
                .withTitle(title)
                .accept(ParameterFluent.class, parameter -> {
                    for (Property property : properties) {
                        parameter.addToProperties(property.getName(), property);
                        if (property.isRequired()) {
                            parameter.addToRequired(property.getName());
                        }
                    }
                }).endParameter();
    }
}
