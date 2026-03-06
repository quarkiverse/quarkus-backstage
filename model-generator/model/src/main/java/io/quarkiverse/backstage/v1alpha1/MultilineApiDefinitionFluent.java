package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class MultilineApiDefinitionFluent<A extends MultilineApiDefinitionFluent<A>> extends BaseFluent<A> {
    public MultilineApiDefinitionFluent() {
    }

    public MultilineApiDefinitionFluent(MultilineApiDefinition instance) {
        this.copyInstance(instance);
    }

    private String value;

    protected void copyInstance(MultilineApiDefinition instance) {
        instance = (instance != null ? instance : new MultilineApiDefinition());

        if (instance != null) {
            this.withValue(instance.getValue());
            this.withValue(instance.getValue());
        }
    }

    public String getValue() {
        return this.value;
    }

    public A withValue(String value) {
        this.value = value;
        return (A) this;
    }

    public boolean hasValue() {
        return this.value != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        MultilineApiDefinitionFluent that = (MultilineApiDefinitionFluent) o;
        if (!java.util.Objects.equals(value, that.value))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(value, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (value != null) {
            sb.append("value:");
            sb.append(value);
        }
        sb.append("}");
        return sb.toString();
    }

}
