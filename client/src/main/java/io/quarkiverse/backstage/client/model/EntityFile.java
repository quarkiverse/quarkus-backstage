package io.quarkiverse.backstage.client.model;

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
        "entity",
        "isRegistered",
        "location"
})
public class EntityFile {

    @JsonProperty("entity")
    private Object entity; // Assuming 'entity' is a JSON object or `Entity` type

    @JsonProperty("isRegistered")
    private Boolean isRegistered;

    @JsonProperty("location")
    private Location location;
}
