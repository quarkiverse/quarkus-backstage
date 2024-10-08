package io.quarkiverse.backstage.v1alpha1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "description",
        "type",
        "default"
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Parameter {

    private String name;
    private String description;
    private String type;
    private String defaultValue;

    public Parameter(String name, String description, String type, String defaultValue) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
    }
}
