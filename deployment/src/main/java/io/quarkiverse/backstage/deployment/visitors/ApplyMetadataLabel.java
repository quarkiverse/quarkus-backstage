package io.quarkiverse.backstage.deployment.visitors;

import java.util.Optional;

import io.quarkiverse.backstage.EntityMetaFluent;
import io.quarkiverse.backstage.model.builder.TypedVisitor;

public class ApplyMetadataLabel extends TypedVisitor<EntityMetaFluent<?>> {

    private final String key;
    private final Optional<String> value;

    public ApplyMetadataLabel(String key, String value) {
        this(key, Optional.of(value));
    }

    public ApplyMetadataLabel(String key, Optional<String> value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void visit(EntityMetaFluent<?> meta) {
        value.ifPresent(v -> {
            meta.addToLabels(key, v);
        });
    }
}
