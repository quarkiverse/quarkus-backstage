package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;

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

    protected void copyInstance(ApiSpec instance) {
        if (instance != null) {
            this.withType(instance.getType());
            this.withLifecycle(instance.getLifecycle());
            this.withOwner(instance.getOwner());
            this.withSystem(instance.getSystem());
            this.withDefinition(instance.getDefinition());
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

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(type, lifecycle, owner, system, definition, super.hashCode());
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
            sb.append(definition);
        }
        sb.append("}");
        return sb.toString();
    }

}
