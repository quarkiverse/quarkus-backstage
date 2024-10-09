package io.quarkiverse.backstage.scaffolder.v1beta3;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
        "title",
        "type",
        "description",
        "properties",
        "default",
        "ui:field"
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Property {

    @JsonIgnore
    private String name;
    private String title;
    private String type;
    private String description;
    @JsonProperty("default")
    private Optional<Object> defaultValue = Optional.empty();
    @JsonProperty("ui:field")
    private Optional<String> uiField = Optional.empty();
    @JsonIgnore
    private boolean required;

    Map<String, Property> properties;
}
