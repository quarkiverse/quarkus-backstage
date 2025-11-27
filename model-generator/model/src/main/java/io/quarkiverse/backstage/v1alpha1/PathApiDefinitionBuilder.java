package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class PathApiDefinitionBuilder extends PathApiDefinitionFluent<PathApiDefinitionBuilder>
        implements VisitableBuilder<PathApiDefinition, PathApiDefinitionBuilder> {
    public PathApiDefinitionBuilder() {
        this(new PathApiDefinition());
    }

    public PathApiDefinitionBuilder(PathApiDefinitionFluent<?> fluent) {
        this(fluent, new PathApiDefinition());
    }

    public PathApiDefinitionBuilder(PathApiDefinitionFluent<?> fluent, PathApiDefinition instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public PathApiDefinitionBuilder(PathApiDefinition instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    PathApiDefinitionFluent<?> fluent;

    public PathApiDefinition build() {
        PathApiDefinition buildable = new PathApiDefinition(fluent.getPath());
        return buildable;
    }

}