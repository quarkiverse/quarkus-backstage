package io.quarkiverse.backstage.v1alpha1;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Presence {

    REQUIRED("required"),
    OPTIONAL("optional");

    private final String value;
    private final static Map<String, Presence> CONSTANTS = new HashMap<>();

    static {
        for (Presence c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    Presence(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static Presence fromValue(String value) {
        Presence constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
