package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.LinkedHashMap;
import java.util.Map;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class ApiSpecFluent<A extends ApiSpecFluent<A>> extends BaseFluent<A> {
    public ApiSpecFluent() {
    }

    public ApiSpecFluent(ApiSpec instance) {
        this.copyInstance(instance);
    }

    private String type;
    private String lifecycle;
    private String owner;
    private String system;
    private String definition;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    protected void copyInstance(ApiSpec instance) {
        instance = (instance != null ? instance : new ApiSpec());

        if (instance != null) {
            this.withType(instance.getType());
            this.withLifecycle(instance.getLifecycle());
            this.withOwner(instance.getOwner());
            this.withSystem(instance.getSystem());
            this.withDefinition(instance.getDefinition());
            this.withAdditionalProperties(instance.getAdditionalProperties());
            this.withType(instance.getType());
            this.withLifecycle(instance.getLifecycle());
            this.withOwner(instance.getOwner());
            this.withSystem(instance.getSystem());
            this.withDefinition(instance.getDefinition());
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

    public String getLifecycle() {
        return this.lifecycle;
    }

    public A withLifecycle(String lifecycle) {
        this.lifecycle = lifecycle;
        return (A) this;
    }

    public boolean hasLifecycle() {
        return this.lifecycle != null;
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

    public String getDefinition() {
        return this.definition;
    }

    public A withDefinition(String definition) {
        this.definition = definition;
        return (A) this;
    }

    public boolean hasDefinition() {
        return this.definition != null;
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
        ApiSpecFluent that = (ApiSpecFluent) o;
        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(lifecycle, that.lifecycle))
            return false;

        if (!java.util.Objects.equals(owner, that.owner))
            return false;

        if (!java.util.Objects.equals(system, that.system))
            return false;

        if (!java.util.Objects.equals(definition, that.definition))
            return false;

        if (!java.util.Objects.equals(additionalProperties, that.additionalProperties))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(type, lifecycle, owner, system, definition, additionalProperties, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (lifecycle != null) {
            sb.append("lifecycle:");
            sb.append(lifecycle + ",");
        }
        if (owner != null) {
            sb.append("owner:");
            sb.append(owner + ",");
        }
        if (system != null) {
            sb.append("system:");
            sb.append(system + ",");
        }
        if (definition != null) {
            sb.append("definition:");
            sb.append(definition + ",");
        }
        if (additionalProperties != null && !additionalProperties.isEmpty()) {
            sb.append("additionalProperties:");
            sb.append(additionalProperties);
        }
        sb.append("}");
        return sb.toString();
    }

}
