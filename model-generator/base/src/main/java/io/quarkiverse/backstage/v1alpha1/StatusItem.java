package io.quarkiverse.backstage.v1alpha1;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "level",
        "message",
        "error",
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StatusItem {
    /**
     * The type of status as a unique key per source.
     */
    private String type;

    /**
     * The level / severity of the status item. If the level is "error", the
     * processing of the entity may be entirely blocked. In this case the status
     * entry may apply to a different, newer version of the data than what is
     * being returned in the catalog response.
     */
    private StatusLevel level;
    /**
     * A brief message describing the status, intended for human consumption.
     */
    private String message;

    /**
     * An optional serialized error object related to the status.
     */
    private Map<String, Object> error;
}
