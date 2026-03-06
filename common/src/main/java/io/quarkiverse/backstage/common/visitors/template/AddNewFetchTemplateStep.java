package io.quarkiverse.backstage.common.visitors.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewFetchTemplateStep extends AddNewTemplateStep {

    private String url;
    private boolean replace;
    private List<String> ignore;
    private Map<String, String> parameters;
    private Map<String, String> values;

    public AddNewFetchTemplateStep(String id, String url, boolean replace, List<String> ignore, Map<String, String> parameters,
            Map<String, String> values) {
        super(id, "Fetch Application Files", "fetch:template");
        this.url = url;
        this.replace = replace;
        this.ignore = ignore;
        this.parameters = parameters;
        this.values = values;

    }

    @Override
    public Map<String, Object> getInput() {
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
