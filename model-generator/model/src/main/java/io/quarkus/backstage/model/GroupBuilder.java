package io.quarkus.backstage.model;

import io.sundr.builder.VisitableBuilder;
public class GroupBuilder extends GroupFluent<GroupBuilder> implements VisitableBuilder<Group,GroupBuilder>{
  public GroupBuilder() {
    this.fluent = this;
  }
  
  public GroupBuilder(GroupFluent<?> fluent) {
    this.fluent = fluent;
  }
  
  public GroupBuilder(GroupFluent<?> fluent,Group instance) {
    this.fluent = fluent;
    fluent.copyInstance(instance);
  }
  
  public GroupBuilder(Group instance) {
    this.fluent = this;
    this.copyInstance(instance);
  }
  GroupFluent<?> fluent;
  
  public Group build() {
    Group buildable = new Group(fluent.getKind(),fluent.getApiVersion(),fluent.buildSpec());
    return buildable;
  }
  

}