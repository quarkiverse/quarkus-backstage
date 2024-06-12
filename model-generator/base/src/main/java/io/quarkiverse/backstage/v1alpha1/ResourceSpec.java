package io.quarkiverse.backstage.v1alpha1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        "owner",
        "dependsOn",
        "system"
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ResourceSpec {
    /**
     * The type of resource.
     * (Required)
     *
     */
    @JsonProperty("type")
    @JsonPropertyDescription("The type of resource.")
    @Size(min = 1)
    @NotNull
    private String type;
    /**
     * An entity reference to the owner of the resource.
     * (Required)
     *
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("An entity reference to the owner of the resource.")
    @Size(min = 1)
    @NotNull
    private String owner;
    /**
     * An array of references to other entities that the resource depends on to function.
     *
     */
    @JsonProperty("dependsOn")
    @JsonPropertyDescription("An array of references to other entities that the resource depends on to function.")
    @Valid
    private List<String> dependsOn;
    /**
     * An entity reference to the system that the resource belongs to.
     *
     */
    @JsonProperty("system")
    @JsonPropertyDescription("An entity reference to the system that the resource belongs to.")
    @Size(min = 1)
    private String system;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

}
