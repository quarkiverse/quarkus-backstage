package io.quarkiverse.backstage.v1alpha1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "apiVersion",
        "kind",
        "metadata",
        "spec",
        "status",
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Template implements Entity {

    private String kind = "Template";
    private final String apiVersion = BACKSTAGE_IO_V1ALPHA1;
    private EntityMeta metadata = new EntityMeta();
    private TemplateSpec spec = new TemplateSpec();
    private Status status;

    public Template(EntityMeta metadata, TemplateSpec spec, Status status) {
        this.metadata = metadata;
        this.spec = spec;
        this.status = status;
    }
}
