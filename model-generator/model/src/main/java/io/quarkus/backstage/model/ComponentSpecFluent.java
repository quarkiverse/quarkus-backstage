package io.quarkus.backstage.model;

import java.lang.SuppressWarnings;
import io.sundr.builder.BaseFluent;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.Object;
import java.util.List;
import java.lang.String;
import java.util.function.Predicate;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class ComponentSpecFluent<A extends ComponentSpecFluent<A>> extends BaseFluent<A>{
  public ComponentSpecFluent() {
  }
  
  public ComponentSpecFluent(ComponentSpec instance) {
    this.copyInstance(instance);
  }
  private String type;
  private String lifecycle;
  private String owner;
  private String system;
  private String subcomponentOf;
  private List<String> providesApis = new ArrayList<String>();
  private List<String> consumesApis = new ArrayList<String>();
  private List<String> dependsOn = new ArrayList<String>();
  
  protected void copyInstance(ComponentSpec instance) {
    if (instance != null) {
        this.withType(instance.getType());
        this.withLifecycle(instance.getLifecycle());
        this.withOwner(instance.getOwner());
        this.withSystem(instance.getSystem());
        this.withSubcomponentOf(instance.getSubcomponentOf());
        this.withProvidesApis(instance.getProvidesApis());
        this.withConsumesApis(instance.getConsumesApis());
        this.withDependsOn(instance.getDependsOn());
      }
  }
  
  public String getType() {
    return this.type;
  }
  
  public A withType(String type) {
    this.type=type; return (A) this;
  }
  
  public boolean hasType() {
    return this.type != null;
  }
  
  public String getLifecycle() {
    return this.lifecycle;
  }
  
  public A withLifecycle(String lifecycle) {
    this.lifecycle=lifecycle; return (A) this;
  }
  
  public boolean hasLifecycle() {
    return this.lifecycle != null;
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
  
  public String getSystem() {
    return this.system;
  }
  
  public A withSystem(String system) {
    this.system=system; return (A) this;
  }
  
  public boolean hasSystem() {
    return this.system != null;
  }
  
  public String getSubcomponentOf() {
    return this.subcomponentOf;
  }
  
  public A withSubcomponentOf(String subcomponentOf) {
    this.subcomponentOf=subcomponentOf; return (A) this;
  }
  
  public boolean hasSubcomponentOf() {
    return this.subcomponentOf != null;
  }
  
  public A addToProvidesApis(int index,String item) {
    if (this.providesApis == null) {this.providesApis = new ArrayList<String>();}
    this.providesApis.add(index, item);
    return (A)this;
  }
  
  public A setToProvidesApis(int index,String item) {
    if (this.providesApis == null) {this.providesApis = new ArrayList<String>();}
    this.providesApis.set(index, item); return (A)this;
  }
  
  public A addToProvidesApis(java.lang.String... items) {
    if (this.providesApis == null) {this.providesApis = new ArrayList<String>();}
    for (String item : items) {this.providesApis.add(item);} return (A)this;
  }
  
  public A addAllToProvidesApis(Collection<String> items) {
    if (this.providesApis == null) {this.providesApis = new ArrayList<String>();}
    for (String item : items) {this.providesApis.add(item);} return (A)this;
  }
  
  public A removeFromProvidesApis(java.lang.String... items) {
    if (this.providesApis == null) return (A)this;
    for (String item : items) { this.providesApis.remove(item);} return (A)this;
  }
  
  public A removeAllFromProvidesApis(Collection<String> items) {
    if (this.providesApis == null) return (A)this;
    for (String item : items) { this.providesApis.remove(item);} return (A)this;
  }
  
  public List<String> getProvidesApis() {
    return this.providesApis;
  }
  
  public String getProvidesApi(int index) {
    return this.providesApis.get(index);
  }
  
  public String getFirstProvidesApi() {
    return this.providesApis.get(0);
  }
  
  public String getLastProvidesApi() {
    return this.providesApis.get(providesApis.size() - 1);
  }
  
  public String getMatchingProvidesApi(Predicate<String> predicate) {
    for (String item: providesApis) { if(predicate.test(item)){ return item;} } return null;
  }
  
  public boolean hasMatchingProvidesApi(Predicate<String> predicate) {
    for (String item: providesApis) { if(predicate.test(item)){ return true;} } return false;
  }
  
  public A withProvidesApis(List<String> providesApis) {
    if (providesApis != null) {this.providesApis = new ArrayList(); for (String item : providesApis){this.addToProvidesApis(item);}} else { this.providesApis = null;} return (A) this;
  }
  
  public A withProvidesApis(java.lang.String... providesApis) {
    if (this.providesApis != null) {this.providesApis.clear(); _visitables.remove("providesApis"); }
    if (providesApis != null) {for (String item :providesApis){ this.addToProvidesApis(item);}} return (A) this;
  }
  
  public boolean hasProvidesApis() {
    return providesApis != null && !providesApis.isEmpty();
  }
  
  public A addToConsumesApis(int index,String item) {
    if (this.consumesApis == null) {this.consumesApis = new ArrayList<String>();}
    this.consumesApis.add(index, item);
    return (A)this;
  }
  
  public A setToConsumesApis(int index,String item) {
    if (this.consumesApis == null) {this.consumesApis = new ArrayList<String>();}
    this.consumesApis.set(index, item); return (A)this;
  }
  
  public A addToConsumesApis(java.lang.String... items) {
    if (this.consumesApis == null) {this.consumesApis = new ArrayList<String>();}
    for (String item : items) {this.consumesApis.add(item);} return (A)this;
  }
  
  public A addAllToConsumesApis(Collection<String> items) {
    if (this.consumesApis == null) {this.consumesApis = new ArrayList<String>();}
    for (String item : items) {this.consumesApis.add(item);} return (A)this;
  }
  
  public A removeFromConsumesApis(java.lang.String... items) {
    if (this.consumesApis == null) return (A)this;
    for (String item : items) { this.consumesApis.remove(item);} return (A)this;
  }
  
  public A removeAllFromConsumesApis(Collection<String> items) {
    if (this.consumesApis == null) return (A)this;
    for (String item : items) { this.consumesApis.remove(item);} return (A)this;
  }
  
  public List<String> getConsumesApis() {
    return this.consumesApis;
  }
  
  public String getConsumesApi(int index) {
    return this.consumesApis.get(index);
  }
  
  public String getFirstConsumesApi() {
    return this.consumesApis.get(0);
  }
  
  public String getLastConsumesApi() {
    return this.consumesApis.get(consumesApis.size() - 1);
  }
  
  public String getMatchingConsumesApi(Predicate<String> predicate) {
    for (String item: consumesApis) { if(predicate.test(item)){ return item;} } return null;
  }
  
  public boolean hasMatchingConsumesApi(Predicate<String> predicate) {
    for (String item: consumesApis) { if(predicate.test(item)){ return true;} } return false;
  }
  
  public A withConsumesApis(List<String> consumesApis) {
    if (consumesApis != null) {this.consumesApis = new ArrayList(); for (String item : consumesApis){this.addToConsumesApis(item);}} else { this.consumesApis = null;} return (A) this;
  }
  
  public A withConsumesApis(java.lang.String... consumesApis) {
    if (this.consumesApis != null) {this.consumesApis.clear(); _visitables.remove("consumesApis"); }
    if (consumesApis != null) {for (String item :consumesApis){ this.addToConsumesApis(item);}} return (A) this;
  }
  
  public boolean hasConsumesApis() {
    return consumesApis != null && !consumesApis.isEmpty();
  }
  
  public A addToDependsOn(int index,String item) {
    if (this.dependsOn == null) {this.dependsOn = new ArrayList<String>();}
    this.dependsOn.add(index, item);
    return (A)this;
  }
  
  public A setToDependsOn(int index,String item) {
    if (this.dependsOn == null) {this.dependsOn = new ArrayList<String>();}
    this.dependsOn.set(index, item); return (A)this;
  }
  
  public A addToDependsOn(java.lang.String... items) {
    if (this.dependsOn == null) {this.dependsOn = new ArrayList<String>();}
    for (String item : items) {this.dependsOn.add(item);} return (A)this;
  }
  
  public A addAllToDependsOn(Collection<String> items) {
    if (this.dependsOn == null) {this.dependsOn = new ArrayList<String>();}
    for (String item : items) {this.dependsOn.add(item);} return (A)this;
  }
  
  public A removeFromDependsOn(java.lang.String... items) {
    if (this.dependsOn == null) return (A)this;
    for (String item : items) { this.dependsOn.remove(item);} return (A)this;
  }
  
  public A removeAllFromDependsOn(Collection<String> items) {
    if (this.dependsOn == null) return (A)this;
    for (String item : items) { this.dependsOn.remove(item);} return (A)this;
  }
  
  public List<String> getDependsOn() {
    return this.dependsOn;
  }
  
  public String getDependsOn(int index) {
    return this.dependsOn.get(index);
  }
  
  public String getFirstDependsOn() {
    return this.dependsOn.get(0);
  }
  
  public String getLastDependsOn() {
    return this.dependsOn.get(dependsOn.size() - 1);
  }
  
  public String getMatchingDependsOn(Predicate<String> predicate) {
    for (String item: dependsOn) { if(predicate.test(item)){ return item;} } return null;
  }
  
  public boolean hasMatchingDependsOn(Predicate<String> predicate) {
    for (String item: dependsOn) { if(predicate.test(item)){ return true;} } return false;
  }
  
  public A withDependsOn(List<String> dependsOn) {
    if (dependsOn != null) {this.dependsOn = new ArrayList(); for (String item : dependsOn){this.addToDependsOn(item);}} else { this.dependsOn = null;} return (A) this;
  }
  
  public A withDependsOn(java.lang.String... dependsOn) {
    if (this.dependsOn != null) {this.dependsOn.clear(); _visitables.remove("dependsOn"); }
    if (dependsOn != null) {for (String item :dependsOn){ this.addToDependsOn(item);}} return (A) this;
  }
  
  public boolean hasDependsOn() {
    return dependsOn != null && !dependsOn.isEmpty();
  }
  
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ComponentSpecFluent that = (ComponentSpecFluent) o;
    if (!java.util.Objects.equals(type, that.type)) return false;
  
    if (!java.util.Objects.equals(lifecycle, that.lifecycle)) return false;
  
    if (!java.util.Objects.equals(owner, that.owner)) return false;
  
    if (!java.util.Objects.equals(system, that.system)) return false;
  
    if (!java.util.Objects.equals(subcomponentOf, that.subcomponentOf)) return false;
  
    if (!java.util.Objects.equals(providesApis, that.providesApis)) return false;
  
    if (!java.util.Objects.equals(consumesApis, that.consumesApis)) return false;
  
    if (!java.util.Objects.equals(dependsOn, that.dependsOn)) return false;
  
    return true;
  }
  
  public int hashCode() {
    return java.util.Objects.hash(type,  lifecycle,  owner,  system,  subcomponentOf,  providesApis,  consumesApis,  dependsOn,  super.hashCode());
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    if (type != null) { sb.append("type:"); sb.append(type + ","); }
    if (lifecycle != null) { sb.append("lifecycle:"); sb.append(lifecycle + ","); }
    if (owner != null) { sb.append("owner:"); sb.append(owner + ","); }
    if (system != null) { sb.append("system:"); sb.append(system + ","); }
    if (subcomponentOf != null) { sb.append("subcomponentOf:"); sb.append(subcomponentOf + ","); }
    if (providesApis != null && !providesApis.isEmpty()) { sb.append("providesApis:"); sb.append(providesApis + ","); }
    if (consumesApis != null && !consumesApis.isEmpty()) { sb.append("consumesApis:"); sb.append(consumesApis + ","); }
    if (dependsOn != null && !dependsOn.isEmpty()) { sb.append("dependsOn:"); sb.append(dependsOn); }
    sb.append("}");
    return sb.toString();
  }
  

}