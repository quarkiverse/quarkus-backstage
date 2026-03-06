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
        result.put("repoUrl", "${{ parameters.repoHost }}?owner=${{ parameters.repoOrg }}&repo=${{ parameters.repoName }}");
        result.put("defaultBranch", "main");
        result.put("repoVisibility", "${{ parameters.repoVisibility }}");
        return result;
    }
}
