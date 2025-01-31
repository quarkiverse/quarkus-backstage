package io.quarkiverse.backstage.common.visitors.template;

import java.util.HashMap;
import java.util.Map;

public class AddPublishGiteaStep extends AddNewTemplateStep {

    public AddPublishGiteaStep(String id) {
        super(id, "Publish to Gitea", "publish:gitea");
    }

    @Override
    public Map<String, Object> getInput() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "${{ parameters.componentId }}");
        result.put("description", "This is ${{ parameters.description }}");
        result.put("repoUrl", "${{ parameters.repo.host }}?owner=${{ parameters.repo.org }}&repo=${{ parameters.repo.name }}");
        result.put("defaultBranch", "main");
        result.put("repoVisibility", "${{ parameters.repo.visibility }}");
        return result;
    }
}
