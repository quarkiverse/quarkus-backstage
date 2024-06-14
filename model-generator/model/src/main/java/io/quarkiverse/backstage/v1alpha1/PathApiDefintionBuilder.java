package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class PathApiDefintionBuilder extends PathApiDefintionFluent<PathApiDefintionBuilder>
        implements VisitableBuilder<PathApiDefintion, PathApiDefintionBuilder> {
    public PathApiDefintionBuilder() {
        this(new PathApiDefintion());
    }

    public PathApiDefintionBuilder(PathApiDefintionFluent<?> fluent) {
        this(fluent, new PathApiDefintion());
    }

    public PathApiDefintionBuilder(PathApiDefintionFluent<?> fluent, PathApiDefintion instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public PathApiDefintionBuilder(PathApiDefintion instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    PathApiDefintionFluent<?> fluent;

    public PathApiDefintion build() {
        PathApiDefintion buildable = new PathApiDefintion(fluent.getPath());
        return buildable;
    }

}