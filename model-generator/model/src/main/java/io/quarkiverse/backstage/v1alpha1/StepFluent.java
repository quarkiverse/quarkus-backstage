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
public class StepFluent<A extends StepFluent<A>> extends BaseFluent<A> {
    public StepFluent() {
    }

    public StepFluent(Step instance) {
        this.copyInstance(instance);
    }

    private String id;
    private String name;
    private String action;
    private Map<String, Object> input = new LinkedHashMap<String, Object>();

    protected void copyInstance(Step instance) {
        instance = (instance != null ? instance : new Step());

        if (instance != null) {
            this.withId(instance.getId());
            this.withName(instance.getName());
            this.withAction(instance.getAction());
            this.withInput(instance.getInput());
            this.withId(instance.getId());
            this.withName(instance.getName());
            this.withAction(instance.getAction());
            this.withInput(instance.getInput());
        }
    }

    public String getId() {
        return this.id;
    }

    public A withId(String id) {
        this.id = id;
        return (A) this;
    }

    public boolean hasId() {
        return this.id != null;
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

    public String getAction() {
        return this.action;
    }

    public A withAction(String action) {
        this.action = action;
        return (A) this;
    }

    public boolean hasAction() {
        return this.action != null;
    }

    public A addToInput(String key, Object value) {
        if (this.input == null && key != null && value != null) {
            this.input = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.input.put(key, value);
        }
        return (A) this;
    }

    public A addToInput(Map<String, Object> map) {
        if (this.input == null && map != null) {
            this.input = new LinkedHashMap();
        }
        if (map != null) {
            this.input.putAll(map);
        }
        return (A) this;
    }

    public A removeFromInput(String key) {
        if (this.input == null) {
            return (A) this;
        }
        if (key != null && this.input != null) {
            this.input.remove(key);
        }
        return (A) this;
    }

    public A removeFromInput(Map<String, Object> map) {
        if (this.input == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.input != null) {
                    this.input.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, Object> getInput() {
        return this.input;
    }

    public <K, V> A withInput(Map<String, Object> input) {
        if (input == null) {
            this.input = null;
        } else {
            this.input = new LinkedHashMap(input);
        }
        return (A) this;
    }

    public boolean hasInput() {
        return this.input != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        StepFluent that = (StepFluent) o;
        if (!java.util.Objects.equals(id, that.id))
            return false;

        if (!java.util.Objects.equals(name, that.name))
            return false;

        if (!java.util.Objects.equals(action, that.action))
            return false;

        if (!java.util.Objects.equals(input, that.input))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(id, name, action, input, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (id != null) {
            sb.append("id:");
            sb.append(id + ",");
        }
        if (name != null) {
            sb.append("name:");
            sb.append(name + ",");
        }
        if (action != null) {
            sb.append("action:");
            sb.append(action + ",");
        }
        if (input != null && !input.isEmpty()) {
            sb.append("input:");
            sb.append(input);
        }
        sb.append("}");
        return sb.toString();
    }

}
