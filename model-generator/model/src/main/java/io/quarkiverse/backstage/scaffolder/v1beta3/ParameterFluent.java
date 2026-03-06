package io.quarkiverse.backstage.scaffolder.v1beta3;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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

    private String title;
    private List<String> required = new ArrayList<String>();
    private Map<String, Property> properties = new LinkedHashMap<String, Property>();
    private Map<String, Object> dependencies = new LinkedHashMap<String, Object>();

    protected void copyInstance(Parameter instance) {
        instance = (instance != null ? instance : new Parameter());

        if (instance != null) {
            this.withTitle(instance.getTitle());
            this.withRequired(instance.getRequired());
            this.withProperties(instance.getProperties());
            this.withDependencies(instance.getDependencies());
            this.withTitle(instance.getTitle());
            this.withRequired(instance.getRequired());
            this.withProperties(instance.getProperties());
            this.withDependencies(instance.getDependencies());
        }
    }

    public String getTitle() {
        return this.title;
    }

    public A withTitle(String title) {
        this.title = title;
        return (A) this;
    }

    public boolean hasTitle() {
        return this.title != null;
    }

    public A addToRequired(int index, String item) {
        if (this.required == null) {
            this.required = new ArrayList<String>();
        }
        this.required.add(index, item);
        return (A) this;
    }

    public A setToRequired(int index, String item) {
        if (this.required == null) {
            this.required = new ArrayList<String>();
        }
        this.required.set(index, item);
        return (A) this;
    }

    public A addToRequired(java.lang.String... items) {
        if (this.required == null) {
            this.required = new ArrayList<String>();
        }
        for (String item : items) {
            this.required.add(item);
        }
        return (A) this;
    }

    public A addAllToRequired(Collection<String> items) {
        if (this.required == null) {
            this.required = new ArrayList<String>();
        }
        for (String item : items) {
            this.required.add(item);
        }
        return (A) this;
    }

    public A removeFromRequired(java.lang.String... items) {
        if (this.required == null)
            return (A) this;
        for (String item : items) {
            this.required.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromRequired(Collection<String> items) {
        if (this.required == null)
            return (A) this;
        for (String item : items) {
            this.required.remove(item);
        }
        return (A) this;
    }

    public List<String> getRequired() {
        return this.required;
    }

    public String getRequired(int index) {
        return this.required.get(index);
    }

    public String getFirstRequired() {
        return this.required.get(0);
    }

    public String getLastRequired() {
        return this.required.get(required.size() - 1);
    }

    public String getMatchingRequired(Predicate<String> predicate) {
        for (String item : required) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingRequired(Predicate<String> predicate) {
        for (String item : required) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withRequired(List<String> required) {
        if (required != null) {
            this.required = new ArrayList();
            for (String item : required) {
                this.addToRequired(item);
            }
        } else {
            this.required = null;
        }
        return (A) this;
    }

    public A withRequired(java.lang.String... required) {
        if (this.required != null) {
            this.required.clear();
            _visitables.remove("required");
        }
        if (required != null) {
            for (String item : required) {
                this.addToRequired(item);
            }
        }
        return (A) this;
    }

    public boolean hasRequired() {
        return required != null && !required.isEmpty();
    }

    public A addToProperties(String key, Property value) {
        if (this.properties == null && key != null && value != null) {
            this.properties = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.properties.put(key, value);
        }
        return (A) this;
    }

    public A addToProperties(Map<String, Property> map) {
        if (this.properties == null && map != null) {
            this.properties = new LinkedHashMap();
        }
        if (map != null) {
            this.properties.putAll(map);
        }
        return (A) this;
    }

    public A removeFromProperties(String key) {
        if (this.properties == null) {
            return (A) this;
        }
        if (key != null && this.properties != null) {
            this.properties.remove(key);
        }
        return (A) this;
    }

    public A removeFromProperties(Map<String, Property> map) {
        if (this.properties == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.properties != null) {
                    this.properties.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, Property> getProperties() {
        return this.properties;
    }

    public <K, V> A withProperties(Map<String, Property> properties) {
        if (properties == null) {
            this.properties = null;
        } else {
            this.properties = new LinkedHashMap(properties);
        }
        return (A) this;
    }

    public boolean hasProperties() {
        return this.properties != null;
    }

    public A addToDependencies(String key, Object value) {
        if (this.dependencies == null && key != null && value != null) {
            this.dependencies = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.dependencies.put(key, value);
        }
        return (A) this;
    }

    public A addToDependencies(Map<String, Object> map) {
        if (this.dependencies == null && map != null) {
            this.dependencies = new LinkedHashMap();
        }
        if (map != null) {
            this.dependencies.putAll(map);
        }
        return (A) this;
    }

    public A removeFromDependencies(String key) {
        if (this.dependencies == null) {
            return (A) this;
        }
        if (key != null && this.dependencies != null) {
            this.dependencies.remove(key);
        }
        return (A) this;
    }

    public A removeFromDependencies(Map<String, Object> map) {
        if (this.dependencies == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.dependencies != null) {
                    this.dependencies.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, Object> getDependencies() {
        return this.dependencies;
    }

    public <K, V> A withDependencies(Map<String, Object> dependencies) {
        if (dependencies == null) {
            this.dependencies = null;
        } else {
            this.dependencies = new LinkedHashMap(dependencies);
        }
        return (A) this;
    }

    public boolean hasDependencies() {
        return this.dependencies != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ParameterFluent that = (ParameterFluent) o;
        if (!java.util.Objects.equals(title, that.title))
            return false;

        if (!java.util.Objects.equals(required, that.required))
            return false;

        if (!java.util.Objects.equals(properties, that.properties))
            return false;

        if (!java.util.Objects.equals(dependencies, that.dependencies))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(title, required, properties, dependencies, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (title != null) {
            sb.append("title:");
            sb.append(title + ",");
        }
        if (required != null && !required.isEmpty()) {
            sb.append("required:");
            sb.append(required + ",");
        }
        if (properties != null && !properties.isEmpty()) {
            sb.append("properties:");
            sb.append(properties + ",");
        }
        if (dependencies != null && !dependencies.isEmpty()) {
            sb.append("dependencies:");
            sb.append(dependencies);
        }
        sb.append("}");
        return sb.toString();
    }

}
