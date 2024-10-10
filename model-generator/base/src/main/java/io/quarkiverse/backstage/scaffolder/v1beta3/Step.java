package io.quarkiverse.backstage.scaffolder.v1beta3;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "action",
        "input"
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Step {

    private String id;
    private String name;
    private String action;
    private Map<String, Object> input;

    public Step(String id, String name, String action, Map<String, Object> input) {
        this.id = id;
        this.name = name;
        this.action = action;
        this.input = input;
    }
}
