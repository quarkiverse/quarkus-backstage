package io.quarkiverse.backstage.client.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstantiateErrorResponse {
    private List<ErrorDetail> errors;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorDetail {
        private List<Object> path;
        private String property;
        private String message;
        private Schema schema;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schema {
        private String title;
        private List<String> required;
        private Map<String, PropertyDetail> properties;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PropertyDetail {
        private String title;
        private String type;
        private String description;
        private String defaultValue;
    }
}
