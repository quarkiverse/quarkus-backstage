package io.quarkus.backstage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "apiVersion",
        "kind",
        "spec",
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Location implements BackstageResource {

    private final String kind;
    private final String apiVersion;
    private final LocationSpec spec;

}
