package io.quarkiverse.backstage.client.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "fields",
        "entity"
})
public class GenerateEntity {

    @JsonProperty("fields")
    private List<Field> fields;

    @JsonProperty("entity")
    private Map<String, Object> entity; // Assuming 'entity' is an arbitrary JSON object
}
