package io.quarkiverse.backstage.client.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "generateEntities",
        "existingEntityFiles"
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AnalyzeLocationResponse {

    @JsonProperty("generateEntities")
    private List<GenerateEntity> generateEntities;

    @JsonProperty("existingEntityFiles")
    private List<EntityFile> existingEntityFiles;
}
