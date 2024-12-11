package io.quarkiverse.backstage.common.visitors.template;

import java.util.HashMap;
import java.util.Map;

public class AddDebugWaitStep extends AddNewTemplateStep {

    private int seconds = 10;

    public AddDebugWaitStep(String id) {
        this(id, 5);
    }

    public AddDebugWaitStep(String id, int seconds) {
        super(id, "Waiting", "debug:wait");
    }

    @Override
    public Map<String, Object> getInput() {
        Map<String, Object> result = new HashMap<>();
        result.put("seconds", seconds);
        return result;
    }
}
