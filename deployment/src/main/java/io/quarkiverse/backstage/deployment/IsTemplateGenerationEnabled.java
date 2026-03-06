package io.quarkiverse.backstage.deployment;

import java.util.function.BooleanSupplier;

import io.quarkiverse.backstage.deployment.devservices.BackstageDevServicesConfig;

public class IsTemplateGenerationEnabled implements BooleanSupplier {

    BackstageConfiguration config;
    BackstageDevServicesConfig devServicesConfig;

    @Override
    public boolean getAsBoolean() {
        return config.template().generation().enabled() || devServicesConfig.template().installation().enabled();
    }
}
