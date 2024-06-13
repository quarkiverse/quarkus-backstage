package io.quarkiverse.backstage.deployment.visitors.component;

import java.util.Optional;

import io.quarkiverse.backstage.deployment.visitors.ApplyMetadataLabel;
import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentFluent;

public class ApplyComponentLabel extends TypedVisitor<ComponentFluent<?>> {

    private final String key;
    private final Optional<String> value;

    public ApplyComponentLabel(String key, String value) {
        this(key, Optional.ofNullable(value));
    }

    public ApplyComponentLabel(String key, Optional<String> value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void visit(ComponentFluent<?> component) {
        component.accept(new ApplyMetadataLabel(key, value));
    }
}
