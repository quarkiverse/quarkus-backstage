package io.quarkus.backstage.model;

import io.sundr.builder.VisitableBuilder;
public class ProfileBuilder extends ProfileFluent<ProfileBuilder> implements VisitableBuilder<Profile,ProfileBuilder>{
  public ProfileBuilder() {
    this.fluent = this;
  }
  
  public ProfileBuilder(ProfileFluent<?> fluent) {
    this.fluent = fluent;
  }
  
  public ProfileBuilder(ProfileFluent<?> fluent,Profile instance) {
    this.fluent = fluent;
    fluent.copyInstance(instance);
  }
  
  public ProfileBuilder(Profile instance) {
    this.fluent = this;
    this.copyInstance(instance);
  }
  ProfileFluent<?> fluent;
  
  public Profile build() {
    Profile buildable = new Profile(fluent.getDisplayName(),fluent.getEmail(),fluent.getPicture(),fluent.getAdditionalProperties());
    return buildable;
  }
  

}