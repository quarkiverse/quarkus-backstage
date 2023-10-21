package io.quarkus.backstage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "lifecycle",
        "owner",
        "system",
        "definition"
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ApiSpec {

    /**
     * The type of the API definition.
     * (Required)
     *
     */
    @JsonProperty("type")
    @JsonPropertyDescription("The type of the API definition.")
    private final String type;
    /**
     * The lifecycle state of the API.
     * (Required)
     *
     */
    @JsonProperty("lifecycle")
    @JsonPropertyDescription("The lifecycle state of the API.")
    private final String lifecycle;
    /**
     * An entity reference to the owner of the API.
     * (Required)
     *
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("An entity reference to the owner of the API.")
    private final String owner;
    /**
     * An entity reference to the system that the API belongs to.
     *
     */
    @JsonProperty("system")
    @JsonPropertyDescription("An entity reference to the system that the API belongs to.")
    private final String system;
}
