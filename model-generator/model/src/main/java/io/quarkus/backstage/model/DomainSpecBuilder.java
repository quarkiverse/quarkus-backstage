package io.quarkus.backstage.model;

import io.sundr.builder.VisitableBuilder;
public class DomainSpecBuilder extends DomainSpecFluent<DomainSpecBuilder> implements VisitableBuilder<DomainSpec,DomainSpecBuilder>{
  public DomainSpecBuilder() {
    this.fluent = this;
  }
  
  public DomainSpecBuilder(DomainSpecFluent<?> fluent) {
    this.fluent = fluent;
  }
  
  public DomainSpecBuilder(DomainSpecFluent<?> fluent,DomainSpec instance) {
    this.fluent = fluent;
    fluent.copyInstance(instance);
  }
  
  public DomainSpecBuilder(DomainSpec instance) {
    this.fluent = this;
    this.copyInstance(instance);
  }
  DomainSpecFluent<?> fluent;
  
  public DomainSpec build() {
    DomainSpec buildable = new DomainSpec(fluent.getOwner());
    return buildable;
  }
  

}