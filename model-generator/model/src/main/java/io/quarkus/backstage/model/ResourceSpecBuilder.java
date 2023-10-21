package io.quarkus.backstage.model;

import io.sundr.builder.VisitableBuilder;
public class ResourceSpecBuilder extends ResourceSpecFluent<ResourceSpecBuilder> implements VisitableBuilder<ResourceSpec,ResourceSpecBuilder>{
  public ResourceSpecBuilder() {
    this.fluent = this;
  }
  
  public ResourceSpecBuilder(ResourceSpecFluent<?> fluent) {
    this.fluent = fluent;
  }
  
  public ResourceSpecBuilder(ResourceSpecFluent<?> fluent,ResourceSpec instance) {
    this.fluent = fluent;
    fluent.copyInstance(instance);
  }
  
  public ResourceSpecBuilder(ResourceSpec instance) {
    this.fluent = this;
    this.copyInstance(instance);
  }
  ResourceSpecFluent<?> fluent;
  
  public ResourceSpec build() {
    ResourceSpec buildable = new ResourceSpec(fluent.getType(),fluent.getOwner(),fluent.getDependsOn(),fluent.getSystem(),fluent.getAdditionalProperties());
    return buildable;
  }
  

}