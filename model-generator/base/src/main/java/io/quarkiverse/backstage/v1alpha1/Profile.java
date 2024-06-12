package io.quarkiverse.backstage.v1alpha1;

import java.util.LinkedHashMap;
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

/**
 * Optional profile information about the group, mainly for display purposes. All fields of this structure are also optional.
 * The email would be a group email of some form, that the group may wish to be used for contacting them. The picture is
 * expected to be a URL pointing to an image that's representative of the group, and that a browser could fetch and render on a
 * group page or similar.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "displayName",
        "email",
        "picture"
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Profile {

    /**
     * A simple display name to present to users.
     *
     */
    @JsonProperty("displayName")
    @JsonPropertyDescription("A simple display name to present to users.")
    @Size(min = 1)
    private String displayName;
    /**
     * An email where this entity can be reached.
     *
     */
    @JsonProperty("email")
    @JsonPropertyDescription("An email where this entity can be reached.")
    @Size(min = 1)
    private String email;
    /**
     * The URL of an image that represents this entity.
     *
     */
    @JsonProperty("picture")
    @JsonPropertyDescription("The URL of an image that represents this entity.")
    @Size(min = 1)
    private String picture;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

}
