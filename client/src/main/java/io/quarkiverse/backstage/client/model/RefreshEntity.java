package io.quarkiverse.backstage.client.model;

import jakarta.validation.constraints.Size;

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
        "entityRef",
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RefreshEntity {

    @JsonProperty("entityRef")
    @JsonPropertyDescription("The refence to the entity to refresh. This can be in the form of kind:namespace/name.")
    @Size(min = 1)
    private String entityRef;

    public RefreshEntity() {
    }

    public RefreshEntity(@Size(min = 1) String entityRef) {
        this.entityRef = entityRef;
    }
}
