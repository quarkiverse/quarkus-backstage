package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class LocationSpecBuilder extends LocationSpecFluent<LocationSpecBuilder>
        implements VisitableBuilder<LocationSpec, LocationSpecBuilder> {
    public LocationSpecBuilder() {
        this(new LocationSpec());
    }

    public LocationSpecBuilder(LocationSpecFluent<?> fluent) {
        this(fluent, new LocationSpec());
    }

    public LocationSpecBuilder(LocationSpecFluent<?> fluent, LocationSpec instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public LocationSpecBuilder(LocationSpec instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    LocationSpecFluent<?> fluent;

    public LocationSpec build() {
        LocationSpec buildable = new LocationSpec(fluent.getType(), fluent.getTarget(), fluent.getTargets(),
                fluent.getPresence(), fluent.getAdditionalProperties());
        return buildable;
    }

}