package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.LinkedHashMap;
import java.util.Map;

import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.Nested;
import io.quarkiverse.backstage.model.builder.VisitableBuilder;

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
    private VisitableBuilder<? extends ApiDefinition, ?> definition;
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

    public ApiDefinition buildDefinition() {
        return this.definition != null ? this.definition.build() : null;
    }

    public A withDefinition(ApiDefinition definition) {
        if (definition == null) {
            this.definition = null;
            _visitables.remove("definition");
            return (A) this;
        }
        VisitableBuilder<? extends ApiDefinition, ?> builder = builder(definition);
        _visitables.get("definition").clear();
        _visitables.get("definition").add(builder);
        this.definition = builder;
        return (A) this;
    }

    public boolean hasDefinition() {
        return this.definition != null;
    }

    public MultilineApiDefintionDefinitionNested<A> withNewMultilineApiDefintionDefinition() {
        return new MultilineApiDefintionDefinitionNested(null);
    }

    public MultilineApiDefintionDefinitionNested<A> withNewMultilineApiDefintionDefinitionLike(MultilineApiDefintion item) {
        return new MultilineApiDefintionDefinitionNested(item);
    }

    public A withNewMultilineApiDefintionDefinition(String value) {
        return (A) withDefinition(new MultilineApiDefintion(value));
    }

    public PathApiDefintionDefinitionNested<A> withNewPathApiDefintionDefinition() {
        return new PathApiDefintionDefinitionNested(null);
    }

    public PathApiDefintionDefinitionNested<A> withNewPathApiDefintionDefinitionLike(PathApiDefintion item) {
        return new PathApiDefintionDefinitionNested(item);
    }

    public A withNewPathApiDefintionDefinition(String path) {
        return (A) withDefinition(new PathApiDefintion(path));
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

    protected static <T> VisitableBuilder<T, ?> builder(Object item) {
        switch (item.getClass().getName()) {
            case "io.quarkiverse.backstage.v1alpha1." + "MultilineApiDefintion":
                return (VisitableBuilder<T, ?>) new MultilineApiDefintionBuilder((MultilineApiDefintion) item);
            case "io.quarkiverse.backstage.v1alpha1." + "PathApiDefintion":
                return (VisitableBuilder<T, ?>) new PathApiDefintionBuilder((PathApiDefintion) item);
        }
        return (VisitableBuilder<T, ?>) builderOf(item);
    }

    public class MultilineApiDefintionDefinitionNested<N>
            extends MultilineApiDefintionFluent<MultilineApiDefintionDefinitionNested<N>> implements Nested<N> {
        MultilineApiDefintionDefinitionNested(MultilineApiDefintion item) {
            this.builder = new MultilineApiDefintionBuilder(this, item);
        }

        MultilineApiDefintionBuilder builder;

        public N and() {
            return (N) ApiSpecFluent.this.withDefinition(builder.build());
        }

        public N endMultilineApiDefintionDefinition() {
            return and();
        }

    }

    public class PathApiDefintionDefinitionNested<N> extends PathApiDefintionFluent<PathApiDefintionDefinitionNested<N>>
            implements Nested<N> {
        PathApiDefintionDefinitionNested(PathApiDefintion item) {
            this.builder = new PathApiDefintionBuilder(this, item);
        }

        PathApiDefintionBuilder builder;

        public N and() {
            return (N) ApiSpecFluent.this.withDefinition(builder.build());
        }

        public N endPathApiDefintionDefinition() {
            return and();
        }

    }

}
