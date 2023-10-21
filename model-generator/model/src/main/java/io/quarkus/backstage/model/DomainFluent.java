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
public class DomainFluent<A extends DomainFluent<A>> extends BaseFluent<A>{
  public DomainFluent() {
  }
  
  public DomainFluent(Domain instance) {
    this.copyInstance(instance);
  }
  private String kind;
  private String apiVersion;
  private DomainSpecBuilder spec;
  
  protected void copyInstance(Domain instance) {
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
  
  public DomainSpec buildSpec() {
    return this.spec!=null ?this.spec.build():null;
  }
  
  public A withSpec(DomainSpec spec) {
    _visitables.get("spec").remove(this.spec);
    if (spec!=null){ this.spec= new DomainSpecBuilder(spec); _visitables.get("spec").add(this.spec);} else { this.spec = null; _visitables.get("spec").remove(this.spec); } return (A) this;
  }
  
  public boolean hasSpec() {
    return this.spec != null;
  }
  
  public A withNewSpec(String owner) {
    return (A)withSpec(new DomainSpec(owner));
  }
  
  public SpecNested<A> withNewSpec() {
    return new SpecNested(null);
  }
  
  public SpecNested<A> withNewSpecLike(DomainSpec item) {
    return new SpecNested(item);
  }
  
  public SpecNested<A> editSpec() {
    return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(null));
  }
  
  public SpecNested<A> editOrNewSpec() {
    return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(new DomainSpecBuilder().build()));
  }
  
  public SpecNested<A> editOrNewSpecLike(DomainSpec item) {
    return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(item));
  }
  
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    DomainFluent that = (DomainFluent) o;
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
  public class SpecNested<N> extends DomainSpecFluent<SpecNested<N>> implements Nested<N>{
    SpecNested(DomainSpec item) {
      this.builder = new DomainSpecBuilder(this, item);
    }
    DomainSpecBuilder builder;
    
    public N and() {
      return (N) DomainFluent.this.withSpec(builder.build());
    }
    
    public N endSpec() {
      return and();
    }
    
  
  }

}