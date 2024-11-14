package io.quarkiverse.backstage.common.visitors.template;

import java.util.Map;

import io.quarkiverse.backstage.model.builder.TypedVisitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.StepFluent;

public class ApplyRegisterGiteaHostedComponentStep extends TypedVisitor<StepFluent<?>> {

    private static final String CATALOG_REGISTER = "catalog:register";

    @Override
    public void visit(StepFluent<?> step) {
        if (CATALOG_REGISTER.equals(step.getAction())) {
            step.withInput(Map.of("catalogInfoUrl",
                    "http://${{ parameters.repo.host }}/${{ parameters.repo.org }}/${{ parameters.repo.name }}/src/branch/${{ parameters.repo.branch}}/catalog-info.yaml"));
        }
    }
}
