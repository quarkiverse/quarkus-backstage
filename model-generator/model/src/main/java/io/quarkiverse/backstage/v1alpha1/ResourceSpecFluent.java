package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class ResourceSpecFluent<A extends ResourceSpecFluent<A>> extends BaseFluent<A> {
    public ResourceSpecFluent() {
    }

    public ResourceSpecFluent(ResourceSpec instance) {
        this.copyInstance(instance);
    }

    private String type;
    private String owner;
    private List<String> dependsOn = new ArrayList<String>();
    private String system;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    protected void copyInstance(ResourceSpec instance) {
        instance = (instance != null ? instance : new ResourceSpec());

        if (instance != null) {
            this.withType(instance.getType());
            this.withOwner(instance.getOwner());
            this.withDependsOn(instance.getDependsOn());
            this.withSystem(instance.getSystem());
            this.withAdditionalProperties(instance.getAdditionalProperties());
            this.withType(instance.getType());
            this.withOwner(instance.getOwner());
            this.withDependsOn(instance.getDependsOn());
            this.withSystem(instance.getSystem());
            this.withAdditionalProperties(instance.getAdditionalProperties());
        }
    }

    public String getType() {
        return this.type;
    }

    public A withType(String type) {
        this.type = type;
        return (A) this;
    }

    public boolean hasType() {
        return this.type != null;
    }

    public String getOwner() {
        return this.owner;
    }

    public A withOwner(String owner) {
        this.owner = owner;
        return (A) this;
    }

    public boolean hasOwner() {
        return this.owner != null;
    }

    public A addToDependsOn(int index, String item) {
        if (this.dependsOn == null) {
            this.dependsOn = new ArrayList<String>();
        }
        this.dependsOn.add(index, item);
        return (A) this;
    }

    public A setToDependsOn(int index, String item) {
        if (this.dependsOn == null) {
            this.dependsOn = new ArrayList<String>();
        }
        this.dependsOn.set(index, item);
        return (A) this;
    }

    public A addToDependsOn(java.lang.String... items) {
        if (this.dependsOn == null) {
            this.dependsOn = new ArrayList<String>();
        }
        for (String item : items) {
            this.dependsOn.add(item);
        }
        return (A) this;
    }

    public A addAllToDependsOn(Collection<String> items) {
        if (this.dependsOn == null) {
            this.dependsOn = new ArrayList<String>();
        }
        for (String item : items) {
            this.dependsOn.add(item);
        }
        return (A) this;
    }

    public A removeFromDependsOn(java.lang.String... items) {
        if (this.dependsOn == null)
            return (A) this;
        for (String item : items) {
            this.dependsOn.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromDependsOn(Collection<String> items) {
        if (this.dependsOn == null)
            return (A) this;
        for (String item : items) {
            this.dependsOn.remove(item);
        }
        return (A) this;
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
        for (String item : dependsOn) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingDependsOn(Predicate<String> predicate) {
        for (String item : dependsOn) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withDependsOn(List<String> dependsOn) {
        if (dependsOn != null) {
            this.dependsOn = new ArrayList();
            for (String item : dependsOn) {
                this.addToDependsOn(item);
            }
        } else {
            this.dependsOn = null;
        }
        return (A) this;
    }

    public A withDependsOn(java.lang.String... dependsOn) {
        if (this.dependsOn != null) {
            this.dependsOn.clear();
            _visitables.remove("dependsOn");
        }
        if (dependsOn != null) {
            for (String item : dependsOn) {
                this.addToDependsOn(item);
            }
        }
        return (A) this;
    }

    public boolean hasDependsOn() {
        return dependsOn != null && !dependsOn.isEmpty();
    }

    public String getSystem() {
        return this.system;
    }

    public A withSystem(String system) {
        this.system = system;
        return (A) this;
    }

    public boolean hasSystem() {
        return this.system != null;
    }

    public A addToAdditionalProperties(String key, Object value) {
        if (this.additionalProperties == null && key != null && value != null) {
            this.additionalProperties = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.additionalProperties.put(key, value);
        }
        return (A) this;
    }

    public A addToAdditionalProperties(Map<String, Object> map) {
        if (this.additionalProperties == null && map != null) {
            this.additionalProperties = new LinkedHashMap();
        }
        if (map != null) {
            this.additionalProperties.putAll(map);
        }
        return (A) this;
    }

    public A removeFromAdditionalProperties(String key) {
        if (this.additionalProperties == null) {
            return (A) this;
        }
        if (key != null && this.additionalProperties != null) {
            this.additionalProperties.remove(key);
        }
        return (A) this;
    }

    public A removeFromAdditionalProperties(Map<String, Object> map) {
        if (this.additionalProperties == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.additionalProperties != null) {
                    this.additionalProperties.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public <K, V> A withAdditionalProperties(Map<String, Object> additionalProperties) {
        if (additionalProperties == null) {
            this.additionalProperties = null;
        } else {
            this.additionalProperties = new LinkedHashMap(additionalProperties);
        }
        return (A) this;
    }

    public boolean hasAdditionalProperties() {
        return this.additionalProperties != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ResourceSpecFluent that = (ResourceSpecFluent) o;
        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(owner, that.owner))
            return false;

        if (!java.util.Objects.equals(dependsOn, that.dependsOn))
            return false;

        if (!java.util.Objects.equals(system, that.system))
            return false;

        if (!java.util.Objects.equals(additionalProperties, that.additionalProperties))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(type, owner, dependsOn, system, additionalProperties, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (owner != null) {
            sb.append("owner:");
            sb.append(owner + ",");
        }
        if (dependsOn != null && !dependsOn.isEmpty()) {
            sb.append("dependsOn:");
            sb.append(dependsOn + ",");
        }
        if (system != null) {
            sb.append("system:");
            sb.append(system + ",");
        }
        if (additionalProperties != null && !additionalProperties.isEmpty()) {
            sb.append("additionalProperties:");
            sb.append(additionalProperties);
        }
        sb.append("}");
        return sb.toString();
    }

}
