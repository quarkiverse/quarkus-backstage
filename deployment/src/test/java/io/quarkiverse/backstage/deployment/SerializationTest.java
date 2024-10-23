package io.quarkiverse.backstage.deployment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.quarkiverse.backstage.v1alpha1.Api;
import io.quarkiverse.backstage.v1alpha1.ApiBuilder;

public class SerializationTest {

    private static YAMLFactory YAML_FACTORY = new YAMLFactory()
            .enable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper(YAML_FACTORY).registerModule(new Jdk8Module())
            .setSerializationInclusion(Include.NON_EMPTY);

    @Test
    public void testApiSerialization() throws JsonProcessingException {
        Api api = new ApiBuilder()
                .withNewSpec()
                .withNewPathApiDefintionDefinition("/openapi")
                .endSpec()
                .build();

        String s = YAML_MAPPER.writeValueAsString(api).replaceAll("!<.*>", "");
        System.out.println(s);
        assertTrue(s.contains("$text: /openapi"));
    }
}
