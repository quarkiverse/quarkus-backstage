package io.quarkiverse.backstage.scaffolder.v1beta3;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

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
    private Map<String, Property> properties = new LinkedHashMap<String, Property>();
    private Optional<Object> defaultValue = Optional.empty();
    private Boolean uiAutoFocus;
    private Optional<String> uiField = Optional.empty();
    private Optional<String> uiWidget = Optional.empty();
    private Map<String, Object> uiOptions = new LinkedHashMap<String, Object>();
    private List<String> enumValues = new ArrayList<String>();
    private List<String> enumNames = new ArrayList<String>();
    private boolean required;

    protected void copyInstance(Property instance) {
        instance = (instance != null ? instance : new Property());

        if (instance != null) {
            this.withName(instance.getName());
            this.withTitle(instance.getTitle());
            this.withType(instance.getType());
            this.withDescription(instance.getDescription());
            this.withProperties(instance.getProperties());
            this.withDefaultValue(instance.getDefaultValue());
            this.withUiAutoFocus(instance.getUiAutoFocus());
            this.withUiField(instance.getUiField());
            this.withUiWidget(instance.getUiWidget());
            this.withUiOptions(instance.getUiOptions());
            this.withEnumValues(instance.getEnumValues());
            this.withEnumNames(instance.getEnumNames());
            this.withRequired(instance.isRequired());
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

    public Boolean getUiAutoFocus() {
        return this.uiAutoFocus;
    }

    public A withUiAutoFocus(Boolean uiAutoFocus) {
        this.uiAutoFocus = uiAutoFocus;
        return (A) this;
    }

    public boolean hasUiAutoFocus() {
        return this.uiAutoFocus != null;
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

    public A withUiWidget(Optional<String> uiWidget) {
        if (uiWidget == null || !uiWidget.isPresent()) {
            this.uiWidget = Optional.empty();
        } else {
            this.uiWidget = uiWidget;
        }
        return (A) this;
    }

    public A withUiWidget(String uiWidget) {
        if (uiWidget == null) {
            this.uiWidget = Optional.empty();
        } else {
            this.uiWidget = Optional.of(uiWidget);
        }
        return (A) this;
    }

    public Optional<String> getUiWidget() {
        return this.uiWidget;
    }

    public boolean hasUiWidget() {
        return uiWidget != null && uiWidget.isPresent();
    }

    public A addToUiOptions(String key, Object value) {
        if (this.uiOptions == null && key != null && value != null) {
            this.uiOptions = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.uiOptions.put(key, value);
        }
        return (A) this;
    }

    public A addToUiOptions(Map<String, Object> map) {
        if (this.uiOptions == null && map != null) {
            this.uiOptions = new LinkedHashMap();
        }
        if (map != null) {
            this.uiOptions.putAll(map);
        }
        return (A) this;
    }

    public A removeFromUiOptions(String key) {
        if (this.uiOptions == null) {
            return (A) this;
        }
        if (key != null && this.uiOptions != null) {
            this.uiOptions.remove(key);
        }
        return (A) this;
    }

    public A removeFromUiOptions(Map<String, Object> map) {
        if (this.uiOptions == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.uiOptions != null) {
                    this.uiOptions.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, Object> getUiOptions() {
        return this.uiOptions;
    }

    public <K, V> A withUiOptions(Map<String, Object> uiOptions) {
        if (uiOptions == null) {
            this.uiOptions = null;
        } else {
            this.uiOptions = new LinkedHashMap(uiOptions);
        }
        return (A) this;
    }

    public boolean hasUiOptions() {
        return this.uiOptions != null;
    }

    public A addToEnumValues(int index, String item) {
        if (this.enumValues == null) {
            this.enumValues = new ArrayList<String>();
        }
        this.enumValues.add(index, item);
        return (A) this;
    }

    public A setToEnumValues(int index, String item) {
        if (this.enumValues == null) {
            this.enumValues = new ArrayList<String>();
        }
        this.enumValues.set(index, item);
        return (A) this;
    }

    public A addToEnumValues(java.lang.String... items) {
        if (this.enumValues == null) {
            this.enumValues = new ArrayList<String>();
        }
        for (String item : items) {
            this.enumValues.add(item);
        }
        return (A) this;
    }

    public A addAllToEnumValues(Collection<String> items) {
        if (this.enumValues == null) {
            this.enumValues = new ArrayList<String>();
        }
        for (String item : items) {
            this.enumValues.add(item);
        }
        return (A) this;
    }

    public A removeFromEnumValues(java.lang.String... items) {
        if (this.enumValues == null)
            return (A) this;
        for (String item : items) {
            this.enumValues.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromEnumValues(Collection<String> items) {
        if (this.enumValues == null)
            return (A) this;
        for (String item : items) {
            this.enumValues.remove(item);
        }
        return (A) this;
    }

    public List<String> getEnumValues() {
        return this.enumValues;
    }

    public String getEnumValue(int index) {
        return this.enumValues.get(index);
    }

    public String getFirstEnumValue() {
        return this.enumValues.get(0);
    }

    public String getLastEnumValue() {
        return this.enumValues.get(enumValues.size() - 1);
    }

    public String getMatchingEnumValue(Predicate<String> predicate) {
        for (String item : enumValues) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingEnumValue(Predicate<String> predicate) {
        for (String item : enumValues) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withEnumValues(List<String> enumValues) {
        if (enumValues != null) {
            this.enumValues = new ArrayList();
            for (String item : enumValues) {
                this.addToEnumValues(item);
            }
        } else {
            this.enumValues = null;
        }
        return (A) this;
    }

    public A withEnumValues(java.lang.String... enumValues) {
        if (this.enumValues != null) {
            this.enumValues.clear();
            _visitables.remove("enumValues");
        }
        if (enumValues != null) {
            for (String item : enumValues) {
                this.addToEnumValues(item);
            }
        }
        return (A) this;
    }

    public boolean hasEnumValues() {
        return enumValues != null && !enumValues.isEmpty();
    }

    public A addToEnumNames(int index, String item) {
        if (this.enumNames == null) {
            this.enumNames = new ArrayList<String>();
        }
        this.enumNames.add(index, item);
        return (A) this;
    }

    public A setToEnumNames(int index, String item) {
        if (this.enumNames == null) {
            this.enumNames = new ArrayList<String>();
        }
        this.enumNames.set(index, item);
        return (A) this;
    }

    public A addToEnumNames(java.lang.String... items) {
        if (this.enumNames == null) {
            this.enumNames = new ArrayList<String>();
        }
        for (String item : items) {
            this.enumNames.add(item);
        }
        return (A) this;
    }

    public A addAllToEnumNames(Collection<String> items) {
        if (this.enumNames == null) {
            this.enumNames = new ArrayList<String>();
        }
        for (String item : items) {
            this.enumNames.add(item);
        }
        return (A) this;
    }

    public A removeFromEnumNames(java.lang.String... items) {
        if (this.enumNames == null)
            return (A) this;
        for (String item : items) {
            this.enumNames.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromEnumNames(Collection<String> items) {
        if (this.enumNames == null)
            return (A) this;
        for (String item : items) {
            this.enumNames.remove(item);
        }
        return (A) this;
    }

    public List<String> getEnumNames() {
        return this.enumNames;
    }

    public String getEnumName(int index) {
        return this.enumNames.get(index);
    }

    public String getFirstEnumName() {
        return this.enumNames.get(0);
    }

    public String getLastEnumName() {
        return this.enumNames.get(enumNames.size() - 1);
    }

    public String getMatchingEnumName(Predicate<String> predicate) {
        for (String item : enumNames) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingEnumName(Predicate<String> predicate) {
        for (String item : enumNames) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withEnumNames(List<String> enumNames) {
        if (enumNames != null) {
            this.enumNames = new ArrayList();
            for (String item : enumNames) {
                this.addToEnumNames(item);
            }
        } else {
            this.enumNames = null;
        }
        return (A) this;
    }

    public A withEnumNames(java.lang.String... enumNames) {
        if (this.enumNames != null) {
            this.enumNames.clear();
            _visitables.remove("enumNames");
        }
        if (enumNames != null) {
            for (String item : enumNames) {
                this.addToEnumNames(item);
            }
        }
        return (A) this;
    }

    public boolean hasEnumNames() {
        return enumNames != null && !enumNames.isEmpty();
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

        if (!java.util.Objects.equals(properties, that.properties))
            return false;

        if (!java.util.Objects.equals(defaultValue, that.defaultValue))
            return false;

        if (!java.util.Objects.equals(uiAutoFocus, that.uiAutoFocus))
            return false;

        if (!java.util.Objects.equals(uiField, that.uiField))
            return false;

        if (!java.util.Objects.equals(uiWidget, that.uiWidget))
            return false;

        if (!java.util.Objects.equals(uiOptions, that.uiOptions))
            return false;

        if (!java.util.Objects.equals(enumValues, that.enumValues))
            return false;

        if (!java.util.Objects.equals(enumNames, that.enumNames))
            return false;

        if (required != that.required)
            return false;
        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(name, title, type, description, properties, defaultValue, uiAutoFocus, uiField, uiWidget,
                uiOptions, enumValues, enumNames, required, super.hashCode());
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
        if (properties != null && !properties.isEmpty()) {
            sb.append("properties:");
            sb.append(properties + ",");
        }
        if (defaultValue != null) {
            sb.append("defaultValue:");
            sb.append(defaultValue + ",");
        }
        if (uiAutoFocus != null) {
            sb.append("uiAutoFocus:");
            sb.append(uiAutoFocus + ",");
        }
        if (uiField != null) {
            sb.append("uiField:");
            sb.append(uiField + ",");
        }
        if (uiWidget != null) {
            sb.append("uiWidget:");
            sb.append(uiWidget + ",");
        }
        if (uiOptions != null && !uiOptions.isEmpty()) {
            sb.append("uiOptions:");
            sb.append(uiOptions + ",");
        }
        if (enumValues != null && !enumValues.isEmpty()) {
            sb.append("enumValues:");
            sb.append(enumValues + ",");
        }
        if (enumNames != null && !enumNames.isEmpty()) {
            sb.append("enumNames:");
            sb.append(enumNames + ",");
        }
        sb.append("required:");
        sb.append(required);
        sb.append("}");
        return sb.toString();
    }

    public A withUiAutoFocus() {
        return withUiAutoFocus(true);
    }

    public A withRequired() {
        return withRequired(true);
    }

}
