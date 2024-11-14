package io.quarkiverse.backstage.common.visitors.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.StepFluent;

public class ApplyPublishGiteaStep extends TypedVisitor<StepFluent<?>> {

    private static List<String> BACKSTAGE_PUBLISH_STEP_ACTIONS = List.of("publish:github", "publish:bitbucket",
            "publish:azure");

    @Override
    public void visit(StepFluent<?> step) {
        if (BACKSTAGE_PUBLISH_STEP_ACTIONS.contains(step.getAction())) {
            step.withId("publish:gitea")
                    .withName("Publish to Gitea")
                    .withAction("publish:gitea")
                    .withInput(createInput(Collections.emptyMap()));
        }
    }

    private static Map<String, Object> createInput(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "${{ parameters.componentId }}");
        result.put("description", "This is ${{ parameters.description }}");
        result.put("repoUrl", "${{ parameters.repo.host }}?owner=${{ parameters.repo.org }}&repo=${{ parameters.repo.name }}");
        result.put("defaultBranch", "main");
        result.put("repoVisibility", "${{ parameters.repo.visibility }}");
        return result;
    }
}
