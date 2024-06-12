package io.quarkiverse.backstage.v1alpha1;

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
     * The url to the external site, document, etc.
     */
    private String url;

    /**
     * An optional descriptive title for the link.
     */
    private Optional<String> title;

    /**
     * An optional semantic key that represents a visual icon.
     */
    private Optional<String> icon;

    /**
     * An optional value to categorize links into specific groups
     */
    private Optional<String> type;
}
