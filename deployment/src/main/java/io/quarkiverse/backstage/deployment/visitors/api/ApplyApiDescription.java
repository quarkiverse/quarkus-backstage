package io.quarkiverse.backstage.deployment.visitors.api;

import io.quarkiverse.backstage.deployment.visitors.ApplyMetadataDescription;
import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ApiFluent;

public class ApplyApiDescription extends TypedVisitor<ApiFluent<?>> {

    private final String description;

    public ApplyApiDescription(String description) {
        this.description = description;
    }

    @Override
    public void visit(ApiFluent<?> api) {
        api.accept(new ApplyMetadataDescription(description));
    }
}
