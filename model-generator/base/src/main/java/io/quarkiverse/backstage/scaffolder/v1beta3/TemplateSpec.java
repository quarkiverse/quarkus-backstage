package io.quarkiverse.backstage.scaffolder.v1beta3;

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
        "owner",
        "system",
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
    private String owner;
    private String system;
    private List<Parameter> parameters;
    private List<Step> steps;
    private Output output;

    public TemplateSpec(String type, String owner, String system, List<Parameter> parameters, List<Step> steps, Output output) {
        this.type = type;
        this.owner = owner;
        this.system = system;
        this.parameters = parameters;
        this.steps = steps;
        this.output = output;
    }
}
