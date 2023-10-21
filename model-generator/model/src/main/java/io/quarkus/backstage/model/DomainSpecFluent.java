package io.quarkus.backstage.model;

import java.lang.SuppressWarnings;
import io.sundr.builder.BaseFluent;
import java.lang.Object;
import java.lang.String;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class DomainSpecFluent<A extends DomainSpecFluent<A>> extends BaseFluent<A>{
  public DomainSpecFluent() {
  }
  
  public DomainSpecFluent(DomainSpec instance) {
    this.copyInstance(instance);
  }
  private String owner;
  
  protected void copyInstance(DomainSpec instance) {
    if (instance != null) {
        this.withOwner(instance.getOwner());
      }
  }
  
  public String getOwner() {
    return this.owner;
  }
  
  public A withOwner(String owner) {
    this.owner=owner; return (A) this;
  }
  
  public boolean hasOwner() {
    return this.owner != null;
  }
  
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    DomainSpecFluent that = (DomainSpecFluent) o;
    if (!java.util.Objects.equals(owner, that.owner)) return false;
  
    return true;
  }
  
  public int hashCode() {
    return java.util.Objects.hash(owner,  super.hashCode());
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    if (owner != null) { sb.append("owner:"); sb.append(owner); }
    sb.append("}");
    return sb.toString();
  }
  

}