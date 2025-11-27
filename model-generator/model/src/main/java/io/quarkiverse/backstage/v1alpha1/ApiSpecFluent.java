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

    public MultilineApiDefinitionDefinitionNested<A> withNewMultilineApiDefinitionDefinition() {
        return new MultilineApiDefinitionDefinitionNested(null);
    }

    public MultilineApiDefinitionDefinitionNested<A> withNewMultilineApiDefinitionDefinitionLike(MultilineApiDefinition item) {
        return new MultilineApiDefinitionDefinitionNested(item);
    }

    public A withNewMultilineApiDefinitionDefinition(String value) {
        return (A) withDefinition(new MultilineApiDefinition(value));
    }

    public PathApiDefinitionDefinitionNested<A> withNewPathApiDefinitionDefinition() {
        return new PathApiDefinitionDefinitionNested(null);
    }

    public PathApiDefinitionDefinitionNested<A> withNewPathApiDefinitionDefinitionLike(PathApiDefinition item) {
        return new PathApiDefinitionDefinitionNested(item);
    }

    public A withNewPathApiDefinitionDefinition(String path) {
        return (A) withDefinition(new PathApiDefinition(path));
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
            case "io.quarkiverse.backstage.v1alpha1." + "MultilineApiDefinition":
                return (VisitableBuilder<T, ?>) new MultilineApiDefinitionBuilder((MultilineApiDefinition) item);
            case "io.quarkiverse.backstage.v1alpha1." + "PathApiDefinition":
                return (VisitableBuilder<T, ?>) new PathApiDefinitionBuilder((PathApiDefinition) item);
        }
        return (VisitableBuilder<T, ?>) builderOf(item);
    }

    public class MultilineApiDefinitionDefinitionNested<N>
            extends MultilineApiDefinitionFluent<MultilineApiDefinitionDefinitionNested<N>> implements Nested<N> {
        MultilineApiDefinitionDefinitionNested(MultilineApiDefinition item) {
            this.builder = new MultilineApiDefinitionBuilder(this, item);
        }

        MultilineApiDefinitionBuilder builder;

        public N and() {
            return (N) ApiSpecFluent.this.withDefinition(builder.build());
        }

        public N endMultilineApiDefinitionDefinition() {
            return and();
        }

    }

    public class PathApiDefinitionDefinitionNested<N> extends PathApiDefinitionFluent<PathApiDefinitionDefinitionNested<N>>
            implements Nested<N> {
        PathApiDefinitionDefinitionNested(PathApiDefinition item) {
            this.builder = new PathApiDefinitionBuilder(this, item);
        }

        PathApiDefinitionBuilder builder;

        public N and() {
            return (N) ApiSpecFluent.this.withDefinition(builder.build());
        }

        public N endPathApiDefinitionDefinition() {
            return and();
        }

    }

}
