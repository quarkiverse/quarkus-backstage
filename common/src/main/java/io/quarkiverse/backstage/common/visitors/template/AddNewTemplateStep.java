package io.quarkiverse.backstage.common.visitors.template;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

public class AddNewTemplateStep extends TypedVisitor<TemplateSpecFluent<?>> {

    private final String id;
    private final String name;
    private final String action;
    private final Map<String, Object> input;
    private final Optional<String> condition;

    public AddNewTemplateStep(String id, String name, String action) {
        this(id, name, action, Collections.emptyMap());
    }

    public AddNewTemplateStep(String id, String name, String action, Map<String, Object> input) {
        this(id, name, action, input, Optional.empty());
    }

    public AddNewTemplateStep(String id, String name, String action, Map<String, Object> input, Optional<String> condition) {
        this.id = id;
        this.name = name;
        this.action = action;
        this.input = input;
        this.condition = condition;
    }

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        spec.addNewStep()
                .withId(id)
                .withName(name)
                .withIf(getCondition().orElse(null))
                .withAction(action)
                .withInput(getInput())
                .endStep();
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public Optional<String> getCondition() {
        return condition;
    }

    @Override
    public int order() {
        return super.order();
    }

}
