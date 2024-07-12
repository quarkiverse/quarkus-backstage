package io.quarkiverse.backstage.deployment.visitors;

import java.util.Optional;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.EntityMetaFluent;

public class ApplyMetadataAnnotation extends TypedVisitor<EntityMetaFluent<?>> {

    private final String key;
    private final Optional<String> value;

    public ApplyMetadataAnnotation(String key, String value) {
        this(key, Optional.of(value));
    }

    public ApplyMetadataAnnotation(String key, Optional<String> value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void visit(EntityMetaFluent<?> meta) {
        value.ifPresent(v -> {
            meta.addToAnnotations(key, v);
        });
    }
}
