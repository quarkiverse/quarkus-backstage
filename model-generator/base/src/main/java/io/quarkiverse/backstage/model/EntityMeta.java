package io.quarkiverse.backstage.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EntityMeta {

    /**
     * A globally unique ID for the entity.
     *
     * This field can not be set by the user at creation time, and the server
     * will reject an attempt to do so. The field will be populated in read
     * operations. The field can (optionally) be specified when performing
     * update or delete operations, but the server is free to reject requests
     * that do so in such a way that it breaks semantics.
     */
    private final Optional<String> uid;

    /**
     * An opaque string that changes for each update operation to any part of
     * the entity, including metadata.
     *
     * This field can not be set by the user at creation time, and the server
     * will reject an attempt to do so. The field will be populated in read
     * operations. The field can (optionally) be specified when performing
     * update or delete operations, and the server will then reject the
     * operation if it does not match the current stored value.
     */
    private final Optional<String> etag;

    /**
     * The name of the entity.
     *
     * Must be unique within the catalog at any given point in time, for any
     * given namespace + kind pair. This value is part of the technical
     * identifier of the entity, and as such it will appear in URLs, database
     * tables, entity references, and similar. It is subject to restrictions
     * regarding what characters are allowed.
     *
     * If you want to use a different, more human readable string with fewer
     * restrictions on it in user interfaces, see the `title` field below.
     */
    private final String name;

    /**
     * The namespace that the entity belongs to.
     */
    private final Optional<String> namespace;

    /**
     * A display name of the entity, to be presented in user interfaces instead
     * of the `name` property above, when available.
     *
     * This field is sometimes useful when the `name` is cumbersome or ends up
     * being perceived as overly technical. The title generally does not have
     * as stringent format requirements on it, so it may contain special
     * characters and be more explanatory. Do keep it very short though, and
     * avoid situations where a title can be confused with the name of another
     * entity, or where two entities share a title.
     *
     * Note that this is only for display purposes, and may be ignored by some
     * parts of the code. Entity references still always make use of the `name`
     * property, not the title.
     */
    private final Optional<String> title;

    /**
     * A short (typically relatively few words, on one line) description of the
     * entity.
     */
    private final Optional<String> description;

    /**
     * Key/value pairs of identifying information attached to the entity.
     */
    private final Map<String, String> labels;

    /**
     * Key/value pairs of non-identifying auxiliary information attached to the
     * entity.
     */
    private final Map<String, String> annotations;

    /**
     * A list of single-valued strings, to for example classify catalog entities in
     * various ways.
     */
    private final List<String> tags;

    /**
     * A list of external hyperlinks related to the entity.
     */
    private final List<EntityLink> links;
}
