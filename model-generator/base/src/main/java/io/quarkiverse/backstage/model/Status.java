package io.quarkiverse.backstage.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "items",
})
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Status {

    private final List<StatusItem> items;

}
