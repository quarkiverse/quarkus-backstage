package io.quarkus.backstage.model;

import io.sundr.builder.VisitableBuilder;
public class ResourceBuilder extends ResourceFluent<ResourceBuilder> implements VisitableBuilder<Resource,ResourceBuilder>{
  public ResourceBuilder() {
    this.fluent = this;
  }
  
  public ResourceBuilder(ResourceFluent<?> fluent) {
    this.fluent = fluent;
  }
  
  public ResourceBuilder(ResourceFluent<?> fluent,Resource instance) {
    this.fluent = fluent;
    fluent.copyInstance(instance);
  }
  
  public ResourceBuilder(Resource instance) {
    this.fluent = this;
    this.copyInstance(instance);
  }
  ResourceFluent<?> fluent;
  
  public Resource build() {
    Resource buildable = new Resource(fluent.getKind(),fluent.getApiVersion(),fluent.buildSpec());
    return buildable;
  }
  

}