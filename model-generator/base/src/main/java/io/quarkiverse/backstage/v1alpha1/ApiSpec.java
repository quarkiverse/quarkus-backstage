package io.quarkiverse.backstage.v1alpha1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Setter
@NoArgsConstructor
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
    private String type;
    /**
     * The lifecycle state of the API.
     * (Required)
     *
     */
    @JsonProperty("lifecycle")
    @JsonPropertyDescription("The lifecycle state of the API.")
    private String lifecycle;
    /**
     * An entity reference to the owner of the API.
     * (Required)
     *
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("An entity reference to the owner of the API.")
    private String owner;
    /**
     * An entity reference to the system that the API belongs to.
     *
     */
    @JsonProperty("system")
    @JsonPropertyDescription("An entity reference to the system that the API belongs to.")
    private String system;

    /**
     * The API definition.
     *
     */
    @JsonProperty("definition")
    @JsonPropertyDescription("The API definition.")
    private String definition;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
