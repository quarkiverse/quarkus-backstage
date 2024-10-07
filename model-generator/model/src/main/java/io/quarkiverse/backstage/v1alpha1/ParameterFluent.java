package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class ParameterFluent<A extends ParameterFluent<A>> extends BaseFluent<A> {
    public ParameterFluent() {
    }

    public ParameterFluent(Parameter instance) {
        this.copyInstance(instance);
    }

    private String name;
    private String description;
    private String type;
    private String defaultValue;

    protected void copyInstance(Parameter instance) {
        instance = (instance != null ? instance : new Parameter());

        if (instance != null) {
            this.withName(instance.getName());
            this.withDescription(instance.getDescription());
            this.withType(instance.getType());
            this.withDefaultValue(instance.getDefaultValue());
            this.withName(instance.getName());
            this.withDescription(instance.getDescription());
            this.withType(instance.getType());
            this.withDefaultValue(instance.getDefaultValue());
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

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public A withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return (A) this;
    }

    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ParameterFluent that = (ParameterFluent) o;
        if (!java.util.Objects.equals(name, that.name))
            return false;

        if (!java.util.Objects.equals(description, that.description))
            return false;

        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(defaultValue, that.defaultValue))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(name, description, type, defaultValue, super.hashCode());
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
            sb.append(description + ",");
        }
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (defaultValue != null) {
            sb.append("defaultValue:");
            sb.append(defaultValue);
        }
        sb.append("}");
        return sb.toString();
    }

}
