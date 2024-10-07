package io.quarkiverse.backstage.v1alpha1;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "parameters",
        "steps",
        "output"
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TemplateSpec {

    private String type;
    private List<Parameter> parameters;
    private List<Step> steps;

    private Output output;

    public TemplateSpec(String type, List<Parameter> parameters, List<Step> steps, Output output) {
        this.type = type;
        this.parameters = parameters;
        this.steps = steps;
        this.output = output;
    }
}
