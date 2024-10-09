package io.quarkiverse.backstage;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EntityLink {

    /**
     * An optional descriptive title for the link.
     */
    private String title;

    /**
     * The url to the external site, document, etc.
     */
    private Optional<String> url;

    /**
     * An optional reference to another entity in the catalog.
     */
    private Optional<String> entityRef;

    /**
     * An optional semantic key that represents a visual icon.
     */
    private Optional<String> icon;

    /**
     * An optional value to categorize links into specific groups
     */
    private Optional<String> type;
}
