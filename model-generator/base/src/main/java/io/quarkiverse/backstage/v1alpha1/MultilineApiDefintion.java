package io.quarkiverse.backstage.v1alpha1;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MultilineApiDefintion implements ApiDefinition {

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
