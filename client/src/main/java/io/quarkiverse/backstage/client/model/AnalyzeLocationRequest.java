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
        "location",
        "catalogFileName"
})
public class AnalyzeLocationRequest {

    @JsonProperty("location")
    private Location loation;

    @JsonProperty("catalogFileName")
    private String catalogFileName;
}
