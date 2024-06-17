package io.quarkiverse.backstage.deployment.visitors.component;

import java.util.List;
import java.util.Optional;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.v1alpha1.ComponentFluent;
import io.quarkiverse.backstage.v1alpha1.ComponentSpecFluent;
import io.quarkiverse.backstage.v1alpha1.EntityMeta;

public class AddComponentDependencies extends TypedVisitor<ComponentFluent<?>> {

    private final String[] dependencies;

    public AddComponentDependencies(List<String> dependencies) {
        this(dependencies.toArray(new String[dependencies.size()]));
    }

    public AddComponentDependencies(String... dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public void visit(ComponentFluent<?> component) {
        component.accept(new AddDependencies(component.buildMetadata(), dependencies));
    }

    private class AddDependencies extends TypedVisitor<ComponentSpecFluent<?>> {
        private final EntityMeta meta;
        private final String[] dependencies;

        public AddDependencies(EntityMeta meta, String... dependencies) {
            this.meta = meta;
            this.dependencies = dependencies;
        }

        @Override
        public void visit(ComponentSpecFluent<?> spec) {
            for (String dependency : dependencies) {
                String namespace = Optional.ofNullable(meta).flatMap(EntityMeta::getNamespace).orElse("default");
                String dependencyRef = "component:" + namespace + "/" + dependency;
                spec.addToDependsOn(dependencyRef);
            }
        }
    }
}
