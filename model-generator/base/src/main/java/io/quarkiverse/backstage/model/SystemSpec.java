package io.quarkiverse.backstage.model;

import java.util.LinkedHashMap;
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
        "owner",
        "domain"
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SystemSpec {

    /**
     * An entity reference to the owner of the component.
     * (Required)
     *
     */
    @JsonProperty("owner")
    @JsonPropertyDescription("An entity reference to the owner of the component.")
    @Size(min = 1)
    @NotNull
    private String owner;
    /**
     * An entity reference to the domain that the system belongs to.
     *
     */
    @JsonProperty("domain")
    @JsonPropertyDescription("An entity reference to the domain that the system belongs to.")
    @Size(min = 1)
    private String domain;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

}
