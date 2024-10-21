package io.quarkiverse.backstage.common.visitors.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddRegisterComponentStep extends AddNewTemplateStep {

    public AddRegisterComponentStep(String id) {
        this(id, Collections.emptyMap());
    }

    public AddRegisterComponentStep(String id, Map<String, Object> parameters) {
        super(id, "Register Component", "catalog:register", createInput(parameters));
    }

    private static Map<String, Object> createInput(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> values = new HashMap<>();
        //This will only work for github
        result.put("catalogInfoUrl",
                "https://${{ parameters.repo.host }}/${{ parameters.repo.org }}/${{ parameters.componentId }}/blob/main/catalog-info.yaml");
        parameters.forEach((k, v) -> values.put(k, "${{ parameters." + k + " }}"));
        return result;
    }
}
