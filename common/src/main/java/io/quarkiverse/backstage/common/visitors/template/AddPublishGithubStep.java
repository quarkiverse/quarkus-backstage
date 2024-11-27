package io.quarkiverse.backstage.common.visitors.template;

import java.util.HashMap;
import java.util.Map;

public class AddPublishGithubStep extends AddNewTemplateStep {

    public AddPublishGithubStep(String id) {
        super(id, "Publish to Github", "publish:github");
    }

    @Override
    public Map<String, Object> getInput() {
        Map<String, Object> result = new HashMap<>();
        result.put("allowedHosts", "['github.com']");
        result.put("description", "This is ${{ parameters.description }}");
        result.put("repoUrl", "${{ parameters.repo.host }}?owner=${{ parameters.repo.org }}&repo=${{ parameters.repo.name }}");
        result.put("defaultBranch", "main");
        result.put("protectDefaultBranch", false);
        result.put("repoVisibility", "${{ parameters.repo.visibility }}");
        return result;
    }
}
