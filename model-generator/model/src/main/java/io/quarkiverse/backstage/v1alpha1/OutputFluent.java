package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class OutputFluent<A extends OutputFluent<A>> extends BaseFluent<A> {
    public OutputFluent() {
    }

    public OutputFluent(Output instance) {
        this.copyInstance(instance);
    }

    private String name;
    private String description;

    protected void copyInstance(Output instance) {
        instance = (instance != null ? instance : new Output());

        if (instance != null) {
            this.withName(instance.getName());
            this.withDescription(instance.getDescription());
            this.withName(instance.getName());
            this.withDescription(instance.getDescription());
        }
    }

    public String getName() {
        return this.name;
    }

    public A withName(String name) {
        this.name = name;
        return (A) this;
    }

    public boolean hasName() {
        return this.name != null;
    }

    public String getDescription() {
        return this.description;
    }

    public A withDescription(String description) {
        this.description = description;
        return (A) this;
    }

    public boolean hasDescription() {
        return this.description != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        OutputFluent that = (OutputFluent) o;
        if (!java.util.Objects.equals(name, that.name))
            return false;

        if (!java.util.Objects.equals(description, that.description))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(name, description, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (name != null) {
            sb.append("name:");
            sb.append(name + ",");
        }
        if (description != null) {
            sb.append("description:");
            sb.append(description);
        }
        sb.append("}");
        return sb.toString();
    }

}
