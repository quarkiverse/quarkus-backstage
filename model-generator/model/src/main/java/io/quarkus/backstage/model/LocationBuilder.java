package io.quarkus.backstage.model;

import io.sundr.builder.VisitableBuilder;
public class LocationBuilder extends LocationFluent<LocationBuilder> implements VisitableBuilder<Location,LocationBuilder>{
  public LocationBuilder() {
    this.fluent = this;
  }
  
  public LocationBuilder(LocationFluent<?> fluent) {
    this.fluent = fluent;
  }
  
  public LocationBuilder(LocationFluent<?> fluent,Location instance) {
    this.fluent = fluent;
    fluent.copyInstance(instance);
  }
  
  public LocationBuilder(Location instance) {
    this.fluent = this;
    this.copyInstance(instance);
  }
  LocationFluent<?> fluent;
  
  public Location build() {
    Location buildable = new Location(fluent.getKind(),fluent.getApiVersion(),fluent.buildSpec());
    return buildable;
  }
  

}