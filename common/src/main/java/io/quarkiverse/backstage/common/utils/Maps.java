package io.quarkiverse.backstage.common.utils;

import java.util.HashMap;
import java.util.Map;

public final class Maps {

    /**
     * Merge a nested map into an existing one.
     *
     * @param existing the existing map.
     * @param map the map that will be merged into the existing.
     * @return the merged map.
     */
    public static Map<String, Object> merge(Map<String, Object> existing, Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(existing);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Object existingValue = result.get(key);
            if (existingValue == null) {
                result.put(key, value);
            } else if (existingValue instanceof Map && value instanceof Map) {
                merge((Map<String, Object>) existingValue, (Map<String, Object>) value);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    public static Map<String, Object> flattenOptionals(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<String, Object> flattenedValue = flattenOptionals((Map<String, Object>) value);
                result.put(key, flattenedValue);
            } else if (value instanceof java.util.Optional) {
                Object flattenedValue = ((java.util.Optional) value).orElse(null);
                result.put(key, flattenedValue);
            } else if (value instanceof java.util.OptionalInt) {
                Object flattenedValue = ((java.util.OptionalInt) value).orElse(0);
                result.put(key, flattenedValue);
            } else if (value instanceof java.util.OptionalLong) {
                Object flattenedValue = ((java.util.OptionalLong) value).orElse(0L);
                result.put(key, flattenedValue);
            } else if (value instanceof java.util.OptionalDouble) {
                Object flattenedValue = ((java.util.OptionalDouble) value).orElse(0.0);
                result.put(key, flattenedValue);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }
}
