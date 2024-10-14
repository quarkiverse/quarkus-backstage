package io.quarkiverse.backstage.common.visitors.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddPublishGithubStep extends AddNewTemplateStep {

    public AddPublishGithubStep(String id) {
        this(id, Collections.emptyMap());
    }

    public AddPublishGithubStep(String id, Map<String, Object> parameters) {
        super(id, "Publish to Github", "publish:github", createInput(parameters));
    }

    private static Map<String, Object> createInput(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> values = new HashMap<>();
        result.put("allowedHosts", "['github.com']");
        result.put("description", "This is ${{ parameters.description }}");
        result.put("repoUrl", "${{ parameters.repo.host }}?owner=${{ parameters.repo.org }}&repo=${{parameters.componentId}}");
        result.put("defaultBranch", "main");
        result.put("protectDefaultBranch", false);
        result.put("repoVisibility", "${{ parameters.repo.visibility }}");
        parameters.forEach((k, v) -> values.put(k, "${{ parameters." + k + " }}"));
        return result;
    }

}
