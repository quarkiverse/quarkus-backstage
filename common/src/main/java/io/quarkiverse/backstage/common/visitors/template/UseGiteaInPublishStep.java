package io.quarkiverse.backstage.common.visitors.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UseGiteaInPublishStep extends AddNewTemplateStep {

    public UseGiteaInPublishStep(String id) {
        this(id, Collections.emptyMap());
    }

    public UseGiteaInPublishStep(String id, Map<String, Object> parameters) {
        super(id, "Publish to Gitea", "publish:gitea", createInput(parameters));
    }

    private static Map<String, Object> createInput(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> values = new HashMap<>();
        result.put("name", "${{ parameters.componentId }}");
        result.put("description", "This is ${{ parameters.description }}");
        result.put("repoUrl", "${{ parameters.repo.host }}?owner=${{ parameters.repo.org }}&repo=${{ parameters.repo.name }}");
        result.put("defaultBranch", "main");
        result.put("repoVisibility", "${{ parameters.repo.visibility }}");
        parameters.forEach((k, v) -> values.put(k, "${{ parameters." + k + " }}"));
        return result;
    }
}
