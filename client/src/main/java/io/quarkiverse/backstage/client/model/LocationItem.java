package io.quarkiverse.backstage.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LocationItem {

    public LocationItem() {
    }

    @JsonProperty("data")
    @JsonPropertyDescription("The data of the location.")
    private LocationEntry data;

}
