package io.quarkiverse.backstage.common.visitors.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.scaffolder.v1beta3.Step;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateBuilder;

public class AddArgoCDCreateResourcesStepTest {

    @Test
    public void shouldGuardStepWithEnabledParameterWhenConfigIsExposed() {
        Template template = newTemplate(new AddArgoCDCreateResourcesStep("deploy", true));

        Step step = template.getSpec().getSteps().get(0);
        assertEquals("${{ parameters.argocd.enabled }}", step.get_if());

        String yaml = Serialization.asYaml(template);
        assertTrue(yaml.contains("if:"));
    }

    @Test
    public void shouldNotGuardStepWhenConfigIsNotExposed() {
        Template template = newTemplate(new AddArgoCDCreateResourcesStep("ci-cd", Optional.of(".argocd/"),
                Optional.of("default"), Optional.of("argocd"), Optional.of("default")));

        Step step = template.getSpec().getSteps().get(0);
        assertNull(step.get_if());

        String yaml = Serialization.asYaml(template);
        assertFalse(yaml.contains("if:"));
    }

    private static Template newTemplate(AddArgoCDCreateResourcesStep step) {
        return new TemplateBuilder()
                .withNewMetadata()
                .withName("my-template")
                .endMetadata()
                .withNewSpec()
                .endSpec()
                .accept(step)
                .build();
    }
}
