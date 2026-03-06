package io.quarkiverse.backstage.cli.common;

import java.util.HashMap;
import java.util.Map;

import io.quarkiverse.backstage.scaffolder.v1beta3.Property;

public final class Properties {

    private Properties() {
        //Utility class
    }

    public static void displayProperty(String name, Property property, String indent) {
        if (property.getProperties() != null) {
            System.out.println(indent + property.getType() + " " + name + ": ");
            property.getProperties().forEach((n, p) -> displayProperty(n, p, indent + "  "));
        } else {
            System.out.println(indent + property.getType() + " " + name + ": " + property.getDescription());
        }
    }

    public static Map<String, Object> extractValues(Map<String, Property> properties) {
        Map<String, Object> values = new HashMap<>();
        for (Map.Entry<String, Property> entry : properties.entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();
            property.getDefaultValue().ifPresentOrElse(value -> values.put(name, value), () -> {
                if (property.getProperties() != null) {
                    values.put(name, extractValues(property.getProperties()));
                }
            });
        }
        return values;
    }
}
