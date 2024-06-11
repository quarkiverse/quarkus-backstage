package io.quarkiverse.backstage.model;

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
        "profile",
        "parent",
        "children",
        "members"
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GroupSpec {
    /**
     * The type of group. There is currently no enforced set of values for this field, so it is left up to the adopting
     * organization to choose a nomenclature that matches their org hierarchy.
     * (Required)
     *
     */
    @JsonProperty("type")
    @JsonPropertyDescription("The type of group. There is currently no enforced set of values for this field, so it is left up to the adopting organization to choose a nomenclature that matches their org hierarchy.")
    @Size(min = 1)
    @NotNull
    private String type;
    /**
     * Optional profile information about the group, mainly for display purposes. All fields of this structure are also
     * optional. The email would be a group email of some form, that the group may wish to be used for contacting them. The
     * picture is expected to be a URL pointing to an image that's representative of the group, and that a browser could fetch
     * and render on a group page or similar.
     *
     */
    @JsonProperty("profile")
    @JsonPropertyDescription("Optional profile information about the group, mainly for display purposes. All fields of this structure are also optional. The email would be a group email of some form, that the group may wish to be used for contacting them. The picture is expected to be a URL pointing to an image that's representative of the group, and that a browser could fetch and render on a group page or similar.")
    @Valid
    private Profile profile;
    /**
     * The immediate parent group in the hierarchy, if any. Not all groups must have a parent; the catalog supports multi-root
     * hierarchies. Groups may however not have more than one parent. This field is an entity reference.
     *
     */
    @JsonProperty("parent")
    @JsonPropertyDescription("The immediate parent group in the hierarchy, if any. Not all groups must have a parent; the catalog supports multi-root hierarchies. Groups may however not have more than one parent. This field is an entity reference.")
    @Size(min = 1)
    private String parent;
    /**
     * The immediate child groups of this group in the hierarchy (whose parent field points to this group). The list must be
     * present, but may be empty if there are no child groups. The items are not guaranteed to be ordered in any particular way.
     * The entries of this array are entity references.
     * (Required)
     *
     */
    @JsonProperty("children")
    @JsonPropertyDescription("The immediate child groups of this group in the hierarchy (whose parent field points to this group). The list must be present, but may be empty if there are no child groups. The items are not guaranteed to be ordered in any particular way. The entries of this array are entity references.")
    @Valid
    @NotNull
    private List<String> children;
    /**
     * The users that are members of this group. The entries of this array are entity references.
     *
     */
    @JsonProperty("members")
    @JsonPropertyDescription("The users that are members of this group. The entries of this array are entity references.")
    @Valid
    private List<String> members;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
}
