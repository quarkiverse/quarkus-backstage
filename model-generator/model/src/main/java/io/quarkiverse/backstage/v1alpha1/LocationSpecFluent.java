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
public class LocationSpecFluent<A extends LocationSpecFluent<A>> extends BaseFluent<A> {
    public LocationSpecFluent() {
    }

    public LocationSpecFluent(LocationSpec instance) {
        this.copyInstance(instance);
    }

    private String type;
    private String target;
    private List<String> targets = new ArrayList<String>();
    private Presence presence;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    protected void copyInstance(LocationSpec instance) {
        if (instance != null) {
            this.withType(instance.getType());
            this.withTarget(instance.getTarget());
            this.withTargets(instance.getTargets());
            this.withPresence(instance.getPresence());
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

    public String getTarget() {
        return this.target;
    }

    public A withTarget(String target) {
        this.target = target;
        return (A) this;
    }

    public boolean hasTarget() {
        return this.target != null;
    }

    public A addToTargets(int index, String item) {
        if (this.targets == null) {
            this.targets = new ArrayList<String>();
        }
        this.targets.add(index, item);
        return (A) this;
    }

    public A setToTargets(int index, String item) {
        if (this.targets == null) {
            this.targets = new ArrayList<String>();
        }
        this.targets.set(index, item);
        return (A) this;
    }

    public A addToTargets(java.lang.String... items) {
        if (this.targets == null) {
            this.targets = new ArrayList<String>();
        }
        for (String item : items) {
            this.targets.add(item);
        }
        return (A) this;
    }

    public A addAllToTargets(Collection<String> items) {
        if (this.targets == null) {
            this.targets = new ArrayList<String>();
        }
        for (String item : items) {
            this.targets.add(item);
        }
        return (A) this;
    }

    public A removeFromTargets(java.lang.String... items) {
        if (this.targets == null)
            return (A) this;
        for (String item : items) {
            this.targets.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromTargets(Collection<String> items) {
        if (this.targets == null)
            return (A) this;
        for (String item : items) {
            this.targets.remove(item);
        }
        return (A) this;
    }

    public List<String> getTargets() {
        return this.targets;
    }

    public String getTarget(int index) {
        return this.targets.get(index);
    }

    public String getFirstTarget() {
        return this.targets.get(0);
    }

    public String getLastTarget() {
        return this.targets.get(targets.size() - 1);
    }

    public String getMatchingTarget(Predicate<String> predicate) {
        for (String item : targets) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingTarget(Predicate<String> predicate) {
        for (String item : targets) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withTargets(List<String> targets) {
        if (targets != null) {
            this.targets = new ArrayList();
            for (String item : targets) {
                this.addToTargets(item);
            }
        } else {
            this.targets = null;
        }
        return (A) this;
    }

    public A withTargets(java.lang.String... targets) {
        if (this.targets != null) {
            this.targets.clear();
            _visitables.remove("targets");
        }
        if (targets != null) {
            for (String item : targets) {
                this.addToTargets(item);
            }
        }
        return (A) this;
    }

    public boolean hasTargets() {
        return targets != null && !targets.isEmpty();
    }

    public Presence getPresence() {
        return this.presence;
    }

    public A withPresence(Presence presence) {
        this.presence = presence;
        return (A) this;
    }

    public boolean hasPresence() {
        return this.presence != null;
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
        LocationSpecFluent that = (LocationSpecFluent) o;
        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(target, that.target))
            return false;

        if (!java.util.Objects.equals(targets, that.targets))
            return false;

        if (!java.util.Objects.equals(presence, that.presence))
            return false;

        if (!java.util.Objects.equals(additionalProperties, that.additionalProperties))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(type, target, targets, presence, additionalProperties, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (target != null) {
            sb.append("target:");
            sb.append(target + ",");
        }
        if (targets != null && !targets.isEmpty()) {
            sb.append("targets:");
            sb.append(targets + ",");
        }
        if (presence != null) {
            sb.append("presence:");
            sb.append(presence + ",");
        }
        if (additionalProperties != null && !additionalProperties.isEmpty()) {
            sb.append("additionalProperties:");
            sb.append(additionalProperties);
        }
        sb.append("}");
        return sb.toString();
    }

}
