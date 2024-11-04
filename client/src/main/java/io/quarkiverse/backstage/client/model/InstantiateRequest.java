package io.quarkiverse.backstage.client.model;

import java.util.Map;

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
        "templateRef",
        "values",
        "secrets"
})
public class InstantiateRequest {

    @JsonProperty("templateRef")
    private String templateRef;
    @JsonProperty("values")
    private Map<String, Object> values;
    @JsonProperty("secrets")
    private Map<String, String> secrets;
}
