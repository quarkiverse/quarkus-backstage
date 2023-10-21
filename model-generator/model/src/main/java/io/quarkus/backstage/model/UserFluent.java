package io.quarkus.backstage.model;

import java.lang.SuppressWarnings;
import io.sundr.builder.BaseFluent;
import io.sundr.builder.Nested;
import java.lang.Object;
import java.lang.String;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class UserFluent<A extends UserFluent<A>> extends BaseFluent<A>{
  public UserFluent() {
  }
  
  public UserFluent(User instance) {
    this.copyInstance(instance);
  }
  private String kind;
  private String apiVersion;
  private UserSpecBuilder spec;
  
  protected void copyInstance(User instance) {
    if (instance != null) {
        this.withKind(instance.getKind());
        this.withApiVersion(instance.getApiVersion());
        this.withSpec(instance.getSpec());
      }
  }
  
  public String getKind() {
    return this.kind;
  }
  
  public A withKind(String kind) {
    this.kind=kind; return (A) this;
  }
  
  public boolean hasKind() {
    return this.kind != null;
  }
  
  public String getApiVersion() {
    return this.apiVersion;
  }
  
  public A withApiVersion(String apiVersion) {
    this.apiVersion=apiVersion; return (A) this;
  }
  
  public boolean hasApiVersion() {
    return this.apiVersion != null;
  }
  
  public UserSpec buildSpec() {
    return this.spec!=null ?this.spec.build():null;
  }
  
  public A withSpec(UserSpec spec) {
    _visitables.get("spec").remove(this.spec);
    if (spec!=null){ this.spec= new UserSpecBuilder(spec); _visitables.get("spec").add(this.spec);} else { this.spec = null; _visitables.get("spec").remove(this.spec); } return (A) this;
  }
  
  public boolean hasSpec() {
    return this.spec != null;
  }
  
  public SpecNested<A> withNewSpec() {
    return new SpecNested(null);
  }
  
  public SpecNested<A> withNewSpecLike(UserSpec item) {
    return new SpecNested(item);
  }
  
  public SpecNested<A> editSpec() {
    return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(null));
  }
  
  public SpecNested<A> editOrNewSpec() {
    return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(new UserSpecBuilder().build()));
  }
  
  public SpecNested<A> editOrNewSpecLike(UserSpec item) {
    return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(item));
  }
  
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    UserFluent that = (UserFluent) o;
    if (!java.util.Objects.equals(kind, that.kind)) return false;
  
    if (!java.util.Objects.equals(apiVersion, that.apiVersion)) return false;
  
    if (!java.util.Objects.equals(spec, that.spec)) return false;
  
    return true;
  }
  
  public int hashCode() {
    return java.util.Objects.hash(kind,  apiVersion,  spec,  super.hashCode());
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    if (kind != null) { sb.append("kind:"); sb.append(kind + ","); }
    if (apiVersion != null) { sb.append("apiVersion:"); sb.append(apiVersion + ","); }
    if (spec != null) { sb.append("spec:"); sb.append(spec); }
    sb.append("}");
    return sb.toString();
  }
  public class SpecNested<N> extends UserSpecFluent<SpecNested<N>> implements Nested<N>{
    SpecNested(UserSpec item) {
      this.builder = new UserSpecBuilder(this, item);
    }
    UserSpecBuilder builder;
    
    public N and() {
      return (N) UserFluent.this.withSpec(builder.build());
    }
    
    public N endSpec() {
      return and();
    }
    
  
  }

}