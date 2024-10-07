package io.quarkiverse.backstage.rest;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "target",
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CreateLocationRequest {

    public CreateLocationRequest() {
    }

    public CreateLocationRequest(@Size(min = 1) String type, @Size(min = 1) String target) {
        this.type = type;
        this.target = target;
    }

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

    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
