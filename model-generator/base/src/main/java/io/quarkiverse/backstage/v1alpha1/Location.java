package io.quarkiverse.backstage.v1alpha1;

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
        "apiVersion",
        "kind",
        "spec",
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Location implements Entity {

    private String kind = "Location";
    private String apiVersion = BACKSTAGE_IO_V1ALPHA1;
    private EntityMeta metadata = new EntityMeta();
    private LocationSpec spec;
    private Status status;
}
