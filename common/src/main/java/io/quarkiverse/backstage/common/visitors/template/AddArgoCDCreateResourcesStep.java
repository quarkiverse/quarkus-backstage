package io.quarkiverse.backstage.common.visitors.template;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AddArgoCDCreateResourcesStep extends AddNewTemplateStep {

    private static final String PARAM_PATH = "${{ parameters.argocd.path }}";
    private static final String PARAM_INSTANCE = "${{ parameters.argocd.instance }}";
    private static final String PARAM_NAMESPACE = "${{ parameters.argocd.namespace }}";
    private static final String PARAM_DESTINATION_NAMESPACE = "${{ parameters.argocd.destination.namespace }}";

    private static final String DEFAULT_PATH = ".argcod/";
    private static final String DEFAULT_INSTANCE = "default";
    private static final String DEFAULT_NAMEPSACE = "argocd";
    private static final String DEFAULT_DESTINATION_NAMEPSACE = "default";

    private final Optional<String> path;
    private final Optional<String> instance;
    private final Optional<String> namespace;
    private final Optional<String> destinationNamespace;

    public AddArgoCDCreateResourcesStep(String id) {
        this(id, false);
    }

    public AddArgoCDCreateResourcesStep(String id, boolean useParams) {
        this(id,
                Optional.of(useParams ? PARAM_PATH : DEFAULT_PATH),
                Optional.of(useParams ? PARAM_INSTANCE : DEFAULT_INSTANCE),
                Optional.of(useParams ? PARAM_NAMESPACE : DEFAULT_NAMEPSACE),
                Optional.of(useParams ? PARAM_DESTINATION_NAMESPACE : DEFAULT_DESTINATION_NAMEPSACE));
    }

    public AddArgoCDCreateResourcesStep(String id, Optional<String> path, Optional<String> instance,
            Optional<String> namespace, Optional<String> destinationNamespace) {
        super(id, "Create ArgoCD Resources", "argocd:create-resources");
        this.path = path;
        this.instance = instance;
        this.namespace = namespace;
        this.destinationNamespace = destinationNamespace;
    }

    @Override
    public Map<String, Object> getInput() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("appName", "${{ parameters.argocd.destination.namespace}}-${{ parameters.componentId }}-bootstrap");
        result.put("projectName", "${{ parameters.componentId }}");
        result.put("namespace", namespace.orElse(DEFAULT_NAMEPSACE));
        result.put("destinationNamespace", destinationNamespace.orElse(DEFAULT_DESTINATION_NAMEPSACE));
        result.put("repoUrl", "https://${{ parameters.repo.host }}/${{ parameters.repo.org }}/${{ parameters.repo.name }}.git");
        result.put("path", path.orElse(DEFAULT_PATH));
        result.put("argoInstance", instance.orElse(DEFAULT_INSTANCE));
        return result;
    }
}
