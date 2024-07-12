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
        "spec",
        "status",
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Api implements Entity {

    private final String kind = "API";
    private final String apiVersion = BACKSTAGE_IO_V1BETA1;
    private EntityMeta metadata = new EntityMeta();
    private ApiSpec spec;
    private Status status;

    public Api(EntityMeta metadata, ApiSpec spec, Status status) {
        this.metadata = metadata;
        this.spec = spec;
        this.status = status;
    }
}
