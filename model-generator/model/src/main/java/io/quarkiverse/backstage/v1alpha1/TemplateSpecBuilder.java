package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class TemplateSpecBuilder extends TemplateSpecFluent<TemplateSpecBuilder>
        implements VisitableBuilder<TemplateSpec, TemplateSpecBuilder> {
    public TemplateSpecBuilder() {
        this(new TemplateSpec());
    }

    public TemplateSpecBuilder(TemplateSpecFluent<?> fluent) {
        this(fluent, new TemplateSpec());
    }

    public TemplateSpecBuilder(TemplateSpecFluent<?> fluent, TemplateSpec instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public TemplateSpecBuilder(TemplateSpec instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    TemplateSpecFluent<?> fluent;

    public TemplateSpec build() {
        TemplateSpec buildable = new TemplateSpec(fluent.getType(), fluent.buildParameters(), fluent.buildSteps(),
                fluent.buildOutput());
        return buildable;
    }

}