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
        "name",
        "description"
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Output {

    private String name;
    private String description;

    public Output(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
