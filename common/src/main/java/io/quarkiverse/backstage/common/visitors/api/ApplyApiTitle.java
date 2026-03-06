package io.quarkiverse.backstage.common.visitors.api;

import io.quarkiverse.backstage.common.visitors.ApplyMetadataTitle;
import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ApiFluent;

public class ApplyApiTitle extends TypedVisitor<ApiFluent<?>> {

    private final String title;

    public ApplyApiTitle(String title) {
        this.title = title;
    }

    @Override
    public void visit(ApiFluent<?> api) {
        api.accept(new ApplyMetadataTitle(title));
    }
}
