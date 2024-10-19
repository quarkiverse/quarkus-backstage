package io.quarkiverse.backstage.common.visitors.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewFetchTemplateStep extends AddNewTemplateStep {

    public AddNewFetchTemplateStep(String id, String url, boolean replace, List<String> ignore, Map<String, String> parameters,
            Map<String, String> values) {
        super(id, "Fetch Application Files", "fetch:template", createInput(url, replace, ignore, parameters, values));
    }

    private static Map<String, Object> createInput(String url, boolean replace, List<String> ignore,
            Map<String, String> parameters, Map<String, String> values) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> newValues = new HashMap<>(values);
        result.put("url", url);
        result.put("replace", replace);
        result.put("copyWithoutTemplating", ignore.toArray(new String[ignore.size()]));
        result.put("values", newValues);
        parameters.forEach((k, v) -> newValues.put(k, "${{ parameters." + k + " }}"));
        return result;
    }
}
