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
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "owner"
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DomainSpec {
    /**
     * An entity reference to the owner of the component.
     * (Required)
     *
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("An entity reference to the owner of the component.")
    private final String owner;

    @JsonIgnore
    private final Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
