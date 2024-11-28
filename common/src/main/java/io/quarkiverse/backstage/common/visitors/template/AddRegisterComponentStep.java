package io.quarkiverse.backstage.common.visitors.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AddRegisterComponentStep extends AddNewTemplateStep {

    private static Function<String, String> GITHUB = catalogInfoPath -> "https://${{ parameters.repo.host }}/${{ parameters.repo.org }}/${{ parameters.repo.name }}/blob/${{ parameters.repo.branch }}/"
            + catalogInfoPath;
    private static Function<String, String> GITEA = catalogInfoPath -> "http://${{ parameters.repo.host }}/${{ parameters.repo.org }}/${{ parameters.repo.name }}/src/branch/${{ parameters.repo.branch}}/"
            + catalogInfoPath;

    private Function<String, String> getCatalogInfoUrl;
    private Map<String, Object> parameters;

    public AddRegisterComponentStep(String id) {
        this(id, GITHUB, Collections.emptyMap());
    }

    public AddRegisterComponentStep(String id, boolean isDevTemplate) {
        this(id, isDevTemplate ? GITEA : GITHUB, Collections.emptyMap());
    }

    public AddRegisterComponentStep(String id, Function<String, String> getCatalogInfoUrl, Map<String, Object> parameters) {
        super(id, "Register Component", "catalog:register");
        this.getCatalogInfoUrl = getCatalogInfoUrl;
        this.parameters = parameters;
    }

    @Override
    public Map<String, Object> getInput() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> values = new HashMap<>();
        result.put("catalogInfoUrl", getCatalogInfoUrl.apply("catalog-info.yaml"));
        parameters.forEach((k, v) -> values.put(k, "${{ parameters." + k + " }}"));
        return result;
    }
}
