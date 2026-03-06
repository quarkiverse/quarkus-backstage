package io.quarkiverse.backstage.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "taskId",
        "type",
        "body",
        "createdAt"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScaffolderEvent {

    @JsonProperty("id")
    private int id;

    @JsonProperty("taskId")
    private String taskId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("body")
    private Body body;

    @JsonProperty("createdAt")
    private String createdAt;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "stepId",
            "message",
            "status",
            "error"
    })
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Body {
        @JsonProperty("stepId")
        private String stepId;

        @JsonProperty("message")
        private String message;

        @JsonProperty("status")
        private String status;

        @JsonProperty("error")
        private ErrorDetail error;

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonPropertyOrder({
                "name",
                "message",
        })

        public static class ErrorDetail {
            @JsonProperty("name")
            private String name;
            @JsonProperty("message")
            private String message;
        }
    }
}
