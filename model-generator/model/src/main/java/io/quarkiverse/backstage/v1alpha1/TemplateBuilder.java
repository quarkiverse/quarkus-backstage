package io.quarkiverse.backstage.v1alpha1;

import io.quarkiverse.backstage.model.builder.VisitableBuilder;

public class TemplateBuilder extends TemplateFluent<TemplateBuilder> implements VisitableBuilder<Template, TemplateBuilder> {
    public TemplateBuilder() {
        this(new Template());
    }

    public TemplateBuilder(TemplateFluent<?> fluent) {
        this(fluent, new Template());
    }

    public TemplateBuilder(TemplateFluent<?> fluent, Template instance) {
        this.fluent = fluent;
        fluent.copyInstance(instance);
    }

    public TemplateBuilder(Template instance) {
        this.fluent = this;
        this.copyInstance(instance);
    }

    TemplateFluent<?> fluent;

    public Template build() {
        Template buildable = new Template(fluent.buildMetadata(), fluent.buildSpec(), fluent.buildStatus());
        buildable.setKind(fluent.getKind());
        return buildable;
    }

}