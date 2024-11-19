package io.quarkiverse.backstage.client.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.quarkiverse.backstage.v1alpha1.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "location",
        "entities"
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CreateLocationResponse {

    public CreateLocationResponse() {
    }

    public CreateLocationResponse(LocationEntry location, List<Entity> entities) {
        this.location = location;
        this.entities = entities;
    }

    @JsonProperty("location")
    @Size(min = 1)
    private LocationEntry location;

    @JsonProperty("entities")
    List<Entity> entities;

    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
