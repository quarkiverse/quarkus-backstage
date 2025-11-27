package io.quarkiverse.backstage.v1alpha1;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ApiDefinition.Deserializer.class)
public interface ApiDefinition {

    public static class Deserializer extends JsonDeserializer<ApiDefinition> {
        @Override
        public ApiDefinition deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            if (node.isTextual()) {
                return new MultilineApiDefinition(node.asText());
            } else if (node.has("$text")) {
                return new PathApiDefinition(node.get("$text").asText());
            }
            return null;
        }
    }
}
