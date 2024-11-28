package io.quarkiverse.backstage.common.visitors.template;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AddArgoCDCreateResourcesStep extends AddNewTemplateStep {

    private static final String PARAM_PATH = "${{ parameters.argocd.path }}";
    private static final String PARAM_INSTANCE = "${{ parameters.argocd.instance }}";
    private static final String PARAM_NAMESPACE = "${{ parameters.argocd.namespace }}";

    private static final String DEFAULT_PATH = ".argcod/";
    private static final String DEFAULT_INSTANCE = "default";
    private static final String DEFAULT_NAMEPSACE = "default";

    private final Optional<String> path;
    private final Optional<String> instance;
    private final Optional<String> namespace;

    public AddArgoCDCreateResourcesStep(String id) {
        this(id, false);
    }

    public AddArgoCDCreateResourcesStep(String id, boolean useParams) {
        this(id,
                Optional.of(useParams ? PARAM_PATH : DEFAULT_PATH),
                Optional.of(useParams ? PARAM_INSTANCE : DEFAULT_INSTANCE),
                Optional.of(useParams ? PARAM_NAMESPACE : DEFAULT_NAMEPSACE));
    }

    public AddArgoCDCreateResourcesStep(String id, Optional<String> path, Optional<String> instance,
            Optional<String> namespace) {
        super(id, "Create ArgoCD Resources", "argocd:create-resources");
        this.path = path;
        this.instance = instance;
        this.namespace = namespace;
    }

    @Override
    public Map<String, Object> getInput() {
        Map<String, Object> result = new HashMap<>();
        result.put("appName",
                String.format("application-%s-${{ parameters.componentId }}", namespace.orElse(DEFAULT_NAMEPSACE)));
        result.put("projectName",
                String.format("appproject-%s-${{ parameters.componentId }}", namespace.orElse(DEFAULT_NAMEPSACE)));
        result.put("namespace", namespace.orElse(DEFAULT_NAMEPSACE));
        result.put("repoUrl", "${{ parameters.repo.host }}?owner=${{ parameters.repo.org }}&repo=${{ parameters.repo.name }}");
        result.put("path", path.orElse(DEFAULT_PATH));
        result.put("argoInstance", instance.orElse(DEFAULT_INSTANCE));
        return result;
    }
}
