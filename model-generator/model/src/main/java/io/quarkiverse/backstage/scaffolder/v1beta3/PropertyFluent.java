package io.quarkiverse.backstage.scaffolder.v1beta3;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class PropertyFluent<A extends PropertyFluent<A>> extends BaseFluent<A> {
    public PropertyFluent() {
    }

    public PropertyFluent(Property instance) {
        this.copyInstance(instance);
    }

    private String name;
    private String title;
    private String type;
    private String description;
    private Optional<Object> defaultValue = Optional.empty();
    private Optional<String> uiField = Optional.empty();
    private boolean required;
    private Map<String, Property> properties = new LinkedHashMap<String, Property>();

    protected void copyInstance(Property instance) {
        instance = (instance != null ? instance : new Property());

        if (instance != null) {
            this.withName(instance.getName());
            this.withTitle(instance.getTitle());
            this.withType(instance.getType());
            this.withDescription(instance.getDescription());
            this.withDefaultValue(instance.getDefaultValue());
            this.withUiField(instance.getUiField());
            this.withRequired(instance.isRequired());
            this.withProperties(instance.getProperties());
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

    public A withDefaultValue(Optional<Object> defaultValue) {
        if (defaultValue == null || !defaultValue.isPresent()) {
            this.defaultValue = Optional.empty();
        } else {
            this.defaultValue = defaultValue;
        }
        return (A) this;
    }

    public A withDefaultValue(Object defaultValue) {
        if (defaultValue == null) {
            this.defaultValue = Optional.empty();
        } else {
            this.defaultValue = Optional.of(defaultValue);
        }
        return (A) this;
    }

    public Optional<Object> getDefaultValue() {
        return this.defaultValue;
    }

    public boolean hasDefaultValue() {
        return defaultValue != null && defaultValue.isPresent();
    }

    public A withUiField(Optional<String> uiField) {
        if (uiField == null || !uiField.isPresent()) {
            this.uiField = Optional.empty();
        } else {
            this.uiField = uiField;
        }
        return (A) this;
    }

    public A withUiField(String uiField) {
        if (uiField == null) {
            this.uiField = Optional.empty();
        } else {
            this.uiField = Optional.of(uiField);
        }
        return (A) this;
    }

    public Optional<String> getUiField() {
        return this.uiField;
    }

    public boolean hasUiField() {
        return uiField != null && uiField.isPresent();
    }

    public boolean isRequired() {
        return this.required;
    }

    public A withRequired(boolean required) {
        this.required = required;
        return (A) this;
    }

    public boolean hasRequired() {
        return true;
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

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        PropertyFluent that = (PropertyFluent) o;
        if (!java.util.Objects.equals(name, that.name))
            return false;

        if (!java.util.Objects.equals(title, that.title))
            return false;

        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(description, that.description))
            return false;

        if (!java.util.Objects.equals(defaultValue, that.defaultValue))
            return false;

        if (!java.util.Objects.equals(uiField, that.uiField))
            return false;

        if (required != that.required)
            return false;
        if (!java.util.Objects.equals(properties, that.properties))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(name, title, type, description, defaultValue, uiField, required, properties,
                super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (name != null) {
            sb.append("name:");
            sb.append(name + ",");
        }
        if (title != null) {
            sb.append("title:");
            sb.append(title + ",");
        }
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (description != null) {
            sb.append("description:");
            sb.append(description + ",");
        }
        if (defaultValue != null) {
            sb.append("defaultValue:");
            sb.append(defaultValue + ",");
        }
        if (uiField != null) {
            sb.append("uiField:");
            sb.append(uiField + ",");
        }
        sb.append("required:");
        sb.append(required + ",");
        if (properties != null && !properties.isEmpty()) {
            sb.append("properties:");
            sb.append(properties);
        }
        sb.append("}");
        return sb.toString();
    }

    public A withRequired() {
        return withRequired(true);
    }

}
