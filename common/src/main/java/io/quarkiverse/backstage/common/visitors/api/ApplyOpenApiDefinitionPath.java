package io.quarkiverse.backstage.common.visitors.api;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ApiSpecFluent;

public class ApplyOpenApiDefinitionPath extends TypedVisitor<ApiSpecFluent<?>> {

    private final String path;

    public ApplyOpenApiDefinitionPath(String path) {
        this.path = path;
    }

    @Override
    public void visit(ApiSpecFluent<?> spec) {
        spec.withNewPathApiDefinitionDefinition(path);
    }
}
