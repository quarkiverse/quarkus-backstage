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
public class StatusItemFluent<A extends StatusItemFluent<A>> extends BaseFluent<A> {
    public StatusItemFluent() {
    }

    public StatusItemFluent(StatusItem instance) {
        this.copyInstance(instance);
    }

    private String type;
    private StatusLevel level;
    private String message;
    private Map<String, Object> error = new LinkedHashMap<String, Object>();

    protected void copyInstance(StatusItem instance) {
        if (instance != null) {
            this.withType(instance.getType());
            this.withLevel(instance.getLevel());
            this.withMessage(instance.getMessage());
            this.withError(instance.getError());
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

    public StatusLevel getLevel() {
        return this.level;
    }

    public A withLevel(StatusLevel level) {
        this.level = level;
        return (A) this;
    }

    public boolean hasLevel() {
        return this.level != null;
    }

    public String getMessage() {
        return this.message;
    }

    public A withMessage(String message) {
        this.message = message;
        return (A) this;
    }

    public boolean hasMessage() {
        return this.message != null;
    }

    public A addToError(String key, Object value) {
        if (this.error == null && key != null && value != null) {
            this.error = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.error.put(key, value);
        }
        return (A) this;
    }

    public A addToError(Map<String, Object> map) {
        if (this.error == null && map != null) {
            this.error = new LinkedHashMap();
        }
        if (map != null) {
            this.error.putAll(map);
        }
        return (A) this;
    }

    public A removeFromError(String key) {
        if (this.error == null) {
            return (A) this;
        }
        if (key != null && this.error != null) {
            this.error.remove(key);
        }
        return (A) this;
    }

    public A removeFromError(Map<String, Object> map) {
        if (this.error == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.error != null) {
                    this.error.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, Object> getError() {
        return this.error;
    }

    public <K, V> A withError(Map<String, Object> error) {
        if (error == null) {
            this.error = null;
        } else {
            this.error = new LinkedHashMap(error);
        }
        return (A) this;
    }

    public boolean hasError() {
        return this.error != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        StatusItemFluent that = (StatusItemFluent) o;
        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(level, that.level))
            return false;

        if (!java.util.Objects.equals(message, that.message))
            return false;

        if (!java.util.Objects.equals(error, that.error))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(type, level, message, error, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (level != null) {
            sb.append("level:");
            sb.append(level + ",");
        }
        if (message != null) {
            sb.append("message:");
            sb.append(message + ",");
        }
        if (error != null && !error.isEmpty()) {
            sb.append("error:");
            sb.append(error);
        }
        sb.append("}");
        return sb.toString();
    }

}
