package io.quarkiverse.backstage.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
        "profile",
        "memberOf"
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserSpec {

    /**
     * Optional profile information about the user, mainly for display purposes. All fields of this structure are also optional.
     * The email would be a primary email of some form, that the user may wish to be used for contacting them. The picture is
     * expected to be a URL pointing to an image that's representative of the user, and that a browser could fetch and render on
     * a profile page or similar.
     *
     */
    @JsonProperty("profile")
    @JsonPropertyDescription("Optional profile information about the user, mainly for display purposes. All fields of this structure are also optional. The email would be a primary email of some form, that the user may wish to be used for contacting them. The picture is expected to be a URL pointing to an image that's representative of the user, and that a browser could fetch and render on a profile page or similar.")
    @Valid
    private Profile profile;
    /**
     * The list of groups that the user is a direct member of (i.e., no transitive memberships are listed here). The list must
     * be present, but may be empty if the user is not member of any groups. The items are not guaranteed to be ordered in any
     * particular way. The entries of this array are entity references.
     * (Required)
     *
     */
    @JsonProperty("memberOf")
    @JsonPropertyDescription("The list of groups that the user is a direct member of (i.e., no transitive memberships are listed here). The list must be present, but may be empty if the user is not member of any groups. The items are not guaranteed to be ordered in any particular way. The entries of this array are entity references.")
    @Valid
    @NotNull
    private List<String> memberOf;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
