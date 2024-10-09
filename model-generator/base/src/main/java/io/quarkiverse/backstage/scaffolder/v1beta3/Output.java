package io.quarkiverse.backstage.scaffolder.v1beta3;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.quarkiverse.backstage.EntityLink;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "links",
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Output {

    private List<EntityLink> links;

    public Output(List<EntityLink> links) {
        this.links = links;
    }
}
