package io.quarkiverse.backstage.v1alpha1;

import java.util.LinkedHashMap;
import java.util.List;
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
        "subcomponentOf",
        "providesApis",
        "consumesApis",
        "dependsOn"
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ComponentSpec {

    /**
     * The type of component.
     * (Required)
     *
     */
    @JsonProperty("type")
    @JsonPropertyDescription("The type of component.")
    private String type;
    /**
     * The lifecycle state of the component.
     * (Required)
     *
     */
    @JsonProperty("lifecycle")
    @JsonPropertyDescription("The lifecycle state of the component.")
    private String lifecycle;
    /**
     * An entity reference to the owner of the component.
     * (Required)
     *
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("An entity reference to the owner of the component.")
    private String owner;
    /**
     * An entity reference to the system that the component belongs to.
     *
     */
    @JsonProperty("system")
    @JsonPropertyDescription("An entity reference to the system that the component belongs to.")
    private String system;
    /**
     * An entity reference to another component of which the component is a part.
     *
     */
    @JsonProperty("subcomponentOf")
    @JsonPropertyDescription("An entity reference to another component of which the component is a part.")
    private String subcomponentOf;
    /**
     * An array of entity references to the APIs that are provided by the component.
     *
     */
    @JsonProperty("providesApis")
    @JsonPropertyDescription("An array of entity references to the APIs that are provided by the component.")
    private List<String> providesApis;
    /**
     * An array of entity references to the APIs that are consumed by the component.
     *
     */
    @JsonProperty("consumesApis")
    @JsonPropertyDescription("An array of entity references to the APIs that are consumed by the component.")
    private List<String> consumesApis;
    /**
     * An array of references to other entities that the component depends on to function.
     *
     */
    @JsonProperty("dependsOn")
    @JsonPropertyDescription("An array of references to other entities that the component depends on to function.")
    private List<String> dependsOn;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
