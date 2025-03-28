package io.quarkiverse.backstage.v1alpha1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.quarkiverse.backstage.EntityMeta;
import io.quarkiverse.backstage.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "apiVersion",
        "kind",
        "spec",
        "status",
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Component implements Entity {

    private String kind = "Component";
    private final String apiVersion = BACKSTAGE_IO_V1ALPHA1;
    private EntityMeta metadata = new EntityMeta();
    private ComponentSpec spec = new ComponentSpec();
    private Status status;

    public Component(EntityMeta metadata, ComponentSpec spec, Status status) {
        this.metadata = metadata;
        this.spec = spec;
        this.status = status;
    }
}
