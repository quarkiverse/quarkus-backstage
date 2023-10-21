package io.quarkus.backstage.model;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.LinkedHashMap;
import java.util.Map;

import io.quarkus.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class SystemSpecFluent<A extends SystemSpecFluent<A>> extends BaseFluent<A> {
    public SystemSpecFluent() {
    }

    public SystemSpecFluent(SystemSpec instance) {
        this.copyInstance(instance);
    }

    private String owner;
    private String domain;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    protected void copyInstance(SystemSpec instance) {
        if (instance != null) {
            this.withOwner(instance.getOwner());
            this.withDomain(instance.getDomain());
            this.withAdditionalProperties(instance.getAdditionalProperties());
        }
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

    public String getDomain() {
        return this.domain;
    }

    public A withDomain(String domain) {
        this.domain = domain;
        return (A) this;
    }

    public boolean hasDomain() {
        return this.domain != null;
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
        SystemSpecFluent that = (SystemSpecFluent) o;
        if (!java.util.Objects.equals(owner, that.owner))
            return false;

        if (!java.util.Objects.equals(domain, that.domain))
            return false;

        if (!java.util.Objects.equals(additionalProperties, that.additionalProperties))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(owner, domain, additionalProperties, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (owner != null) {
            sb.append("owner:");
            sb.append(owner + ",");
        }
        if (domain != null) {
            sb.append("domain:");
            sb.append(domain + ",");
        }
        if (additionalProperties != null && !additionalProperties.isEmpty()) {
            sb.append("additionalProperties:");
            sb.append(additionalProperties);
        }
        sb.append("}");
        return sb.toString();
    }

}
