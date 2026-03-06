package io.quarkiverse.backstage.deployment;

import java.util.function.BooleanSupplier;

import io.quarkiverse.backstage.deployment.devservices.BackstageDevServicesConfig;

public class IsDevTemplateGenerationEnabled implements BooleanSupplier {

    BackstageConfiguration config;
    BackstageDevServicesConfig devServicesConfig;

    @Override
    public boolean getAsBoolean() {
        return config.devTemplate().generation().enabled() || devServicesConfig.devTemplate().installation().enabled();
    }

}
