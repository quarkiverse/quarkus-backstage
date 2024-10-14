package io.quarkiverse.backstage.deployment.visitors.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewFetchTemplateStep extends AddNewTemplateStep {

    public AddNewFetchTemplateStep(String id, String url, boolean replace, List<String> ignore,
            Map<String, String> parameters) {
        super(id, "Fetch Application Files", "fetch:template", createInput(url, replace, ignore, parameters));
    }

    private static Map<String, Object> createInput(String url, boolean replace, List<String> ignore,
            Map<String, String> parameters) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> values = new HashMap<>();
        result.put("url", url);
        result.put("replace", replace);
        result.put("copyWithoutTemplating", ignore.toArray(new String[ignore.size()]));
        result.put("values", values);
        parameters.forEach((k, v) -> values.put(k, "${{ parameters." + k + " }}"));
        return result;
    }
}
