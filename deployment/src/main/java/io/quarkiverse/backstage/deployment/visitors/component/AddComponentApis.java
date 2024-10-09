package io.quarkiverse.backstage.deployment.visitors.component;

import java.util.List;
import java.util.Optional;

import io.quarkiverse.backstage.EntityMeta;
import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentFluent;
import io.quarkiverse.backstage.v1alpha1.ComponentSpecFluent;

public class AddComponentApis extends TypedVisitor<ComponentFluent<?>> {

    private final String[] apis;

    public AddComponentApis(List<String> apis) {
        this(apis.toArray(new String[apis.size()]));
    }

    public AddComponentApis(String... apis) {
        this.apis = apis;
    }

    @Override
    public void visit(ComponentFluent<?> component) {
        component.accept(new AddApis(component.buildMetadata(), apis));
    }

    private class AddApis extends TypedVisitor<ComponentSpecFluent<?>> {
        private final EntityMeta meta;
        private final String[] apis;

        public AddApis(EntityMeta meta, String... apis) {
            this.meta = meta;
            this.apis = apis;
        }

        @Override
        public void visit(ComponentSpecFluent<?> spec) {
            for (String api : apis) {
                String namespace = Optional.ofNullable(meta).flatMap(EntityMeta::getNamespace).orElse("default");
                String apiRef = "api:" + namespace + "/" + api;
                spec.addToProvidesApis(api);
            }
        }
    }
}
