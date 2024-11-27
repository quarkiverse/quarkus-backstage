package io.quarkiverse.backstage.scaffolder.v1beta3;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class PropertyBuilder extends PropertyFluent<PropertyBuilder> implements VisitableBuilder<Property, PropertyBuilder> {
    public PropertyBuilder() {
        this(new Property());
    }

    public PropertyBuilder(PropertyFluent<?> fluent) {
        this(fluent, new Property());
    }

    public PropertyBuilder(PropertyFluent<?> fluent, Property instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public PropertyBuilder(Property instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    PropertyFluent<?> fluent;

    public Property build() {
        Property buildable = new Property();
        buildable.setName(fluent.getName());
        buildable.setTitle(fluent.getTitle());
        buildable.setType(fluent.getType());
        buildable.setDescription(fluent.getDescription());
        buildable.setProperties(fluent.getProperties());
        buildable.setDefaultValue(fluent.getDefaultValue());
        buildable.setUiAutoFocus(fluent.getUiAutoFocus());
        buildable.setUiField(fluent.getUiField());
        buildable.setUiWidget(fluent.getUiWidget());
        buildable.setUiOptions(fluent.getUiOptions());
        buildable.setEnumValues(fluent.getEnumValues());
        buildable.setEnumNames(fluent.getEnumNames());
        buildable.setRequired(fluent.isRequired());
        return buildable;
    }

}