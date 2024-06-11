package io.quarkiverse.backstage.model;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EntityLink {
    /**
     * The url to the external site, document, etc.
     */
    private final String url;

    /**
     * An optional descriptive title for the link.
     */
    private final Optional<String> title;

    /**
     * An optional semantic key that represents a visual icon.
     */
    private Optional<String> icon;

    /**
     * An optional value to categorize links into specific groups
     */
    private Optional<String> type;
}
