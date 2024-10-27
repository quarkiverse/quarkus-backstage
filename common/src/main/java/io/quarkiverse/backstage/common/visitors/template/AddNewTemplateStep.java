package io.quarkiverse.backstage.common.visitors.template;

import java.util.Map;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

public class AddNewTemplateStep extends TypedVisitor<TemplateSpecFluent<?>> {

    private final String id;
    private final String name;
    private final String action;
    private final Map<String, Object> input;

    public AddNewTemplateStep(String id, String name, String action, Map<String, Object> input) {
        this.id = id;
        this.name = name;
        this.action = action;
        this.input = input;
    }

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        spec.addNewStep()
                .withId(id)
                .withName(name)
                .withAction(action)
                .withInput(input)
                .endStep();
    }

    @Override
    public int order() {
        return super.order();
    }

}
