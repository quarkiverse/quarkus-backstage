package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class LocationBuilder extends LocationFluent<LocationBuilder> implements VisitableBuilder<Location, LocationBuilder> {
    public LocationBuilder() {
        this(new Location());
    }

    public LocationBuilder(LocationFluent<?> fluent) {
        this(fluent, new Location());
    }

    public LocationBuilder(LocationFluent<?> fluent, Location instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public LocationBuilder(Location instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    LocationFluent<?> fluent;

    public Location build() {
        Location buildable = new Location(fluent.buildMetadata(), fluent.buildSpec(), fluent.buildStatus());
        return buildable;
    }

}