package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class PathApiDefintionFluent<A extends PathApiDefintionFluent<A>> extends BaseFluent<A> {
    public PathApiDefintionFluent() {
    }

    public PathApiDefintionFluent(PathApiDefintion instance) {
        this.copyInstance(instance);
    }

    private String path;

    protected void copyInstance(PathApiDefintion instance) {
        instance = (instance != null ? instance : new PathApiDefintion());

        if (instance != null) {
            this.withPath(instance.getPath());
            this.withPath(instance.getPath());
        }
    }

    public String getPath() {
        return this.path;
    }

    public A withPath(String path) {
        this.path = path;
        return (A) this;
    }

    public boolean hasPath() {
        return this.path != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        PathApiDefintionFluent that = (PathApiDefintionFluent) o;
        if (!java.util.Objects.equals(path, that.path))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(path, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (path != null) {
            sb.append("path:");
            sb.append(path);
        }
        sb.append("}");
        return sb.toString();
    }

}
