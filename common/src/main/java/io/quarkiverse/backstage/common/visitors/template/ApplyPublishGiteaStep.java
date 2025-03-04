package io.quarkiverse.backstage.common.visitors.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

public class ApplyPublishGiteaStep extends TypedVisitor<TemplateSpecFluent<?>> {

    private static List<String> BACKSTAGE_PUBLISH_STEP_ACTIONS = List.of("publish:github", "publish:bitbucket",
            "publish:azure");

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        spec.editMatchingStep(step -> BACKSTAGE_PUBLISH_STEP_ACTIONS.contains(step.getAction()))
                .withId("publish:gitea")
                .withName("Publish to Gitea")
                .withAction("publish:gitea")
                .withInput(createInput(Collections.emptyMap()))
                .endStep();
    }

    private static Map<String, Object> createInput(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "${{ parameters.componentId }}");
        result.put("description", "This is ${{ parameters.description }}");
        result.put("repoUrl", "${{ parameters.repoHost }}?owner=${{ parameters.repoOrg }}&repo=${{ parameters.repoName }}");
        result.put("defaultBranch", "main");
        result.put("repoVisibility", "${{ parameters.repoVisibility }}");
        return result;
    }
}
