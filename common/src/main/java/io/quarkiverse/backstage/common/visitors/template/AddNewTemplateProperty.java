package io.quarkiverse.backstage.common.visitors.template;

import java.util.Optional;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.ParameterFluent;
import io.quarkiverse.backstage.scaffolder.v1beta3.PropertyBuilder;
import io.quarkiverse.backstage.scaffolder.v1beta3.PropertyFluent;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

@SuppressWarnings("unchecked")
public class AddNewTemplateProperty extends TypedVisitor<TemplateSpecFluent<?>> {

    private final String parameter;
    private final String name;
    private final String title;
    private final String type;
    private final String description;
    private final boolean required;
    private final Optional<String> defaultValue;

    public AddNewTemplateProperty(String parameter, String name, String title, String type, String description,
            boolean required, Optional<String> defaultValue) {
        this.parameter = parameter;
        this.name = name;
        this.title = title;
        this.type = type;
        this.description = description;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        if (!spec.hasMatchingParameter(p -> p.getTitle().equals(parameter))) {
            spec.addNewParameter()
                    .withTitle(parameter)
                    .endParameter();
        }

        spec.editMatchingParameter(p -> p.getTitle().equals(parameter))
                .accept(ParameterFluent.class, parameter -> {
                    if (required)
                        parameter.getRequired().add(name);
                })
                .accept(ParameterFluent.class, parameter -> parameter.getProperties()
                        .put(name, new PropertyBuilder()
                                .withTitle(title)
                                .withType(type)
                                .withDescription(description)
                                .accept(PropertyFluent.class, p -> defaultValue.ifPresent(p::withDefaultValue))
                                .build()))
                .endParameter();
    }
}
