package io.quarkiverse.backstage.v1alpha1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

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
        "target",
        "targets",
        "presence"
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LocationSpec {
    /**
     * The single location type, that's common to the targets specified in the spec. If it is left out, it is inherited from the
     * location type that originally read the entity data.
     *
     */
    @JsonProperty("type")
    @JsonPropertyDescription("The single location type, that's common to the targets specified in the spec. If it is left out, it is inherited from the location type that originally read the entity data.")
    @Size(min = 1)
    private String type;
    /**
     * A single target as a string. Can be either an absolute path/URL (depending on the type), or a relative path such as
     * ./details/catalog-info.yaml which is resolved relative to the location of this Location entity itself.
     *
     */
    @JsonProperty("target")
    @JsonPropertyDescription("A single target as a string. Can be either an absolute path/URL (depending on the type), or a relative path such as ./details/catalog-info.yaml which is resolved relative to the location of this Location entity itself.")
    @Size(min = 1)
    private String target;
    /**
     * A list of targets as strings. They can all be either absolute paths/URLs (depending on the type), or relative paths such
     * as ./details/catalog-info.yaml which are resolved relative to the location of this Location entity itself.
     *
     */
    @JsonProperty("targets")
    @JsonPropertyDescription("A list of targets as strings. They can all be either absolute paths/URLs (depending on the type), or relative paths such as ./details/catalog-info.yaml which are resolved relative to the location of this Location entity itself.")
    @Valid
    private List<String> targets;
    /**
     * Whether the presence of the location target is required and it should be considered an error if it can not be found
     *
     */
    @JsonProperty("presence")
    @JsonPropertyDescription("Whether the presence of the location target is required and it should be considered an error if it can not be found")
    private Presence presence = Presence.fromValue("required");
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
