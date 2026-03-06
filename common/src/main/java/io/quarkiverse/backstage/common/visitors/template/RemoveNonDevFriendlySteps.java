package io.quarkiverse.backstage.common.visitors.template;

import java.util.List;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateSpecFluent;

public class RemoveNonDevFriendlySteps extends TypedVisitor<TemplateSpecFluent<?>> {

    private final List<String> devFriendlyActions;

    public RemoveNonDevFriendlySteps(List<String> devFriendlyActions) {
        this.devFriendlyActions = devFriendlyActions;
    }

    @Override
    public void visit(TemplateSpecFluent<?> spec) {
        spec.removeMatchingFromSteps(step -> !devFriendlyActions.contains(step.getAction()));
    }
}
