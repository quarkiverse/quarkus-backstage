package io.quarkiverse.backstage.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "description",
        "value",
        "state",
        "field"
})
public class Field {

    @JsonProperty("description")
    private String description;

    @JsonProperty("value")
    private Object value; // Value can be either a String or an Object

    @JsonProperty("state")
    private String state;

    @JsonProperty("field")
    private String field;
}
