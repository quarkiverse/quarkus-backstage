package io.quarkiverse.backstage.deployment;

import java.util.function.BooleanSupplier;

import io.quarkiverse.backstage.deployment.devservices.BackstageDevServicesConfig;

public class IsUserProvidedTemplateGenerationEnabled implements BooleanSupplier {

    BackstageConfiguration config;
    BackstageDevServicesConfig devServicesConfig;

    @Override
    public boolean getAsBoolean() {
        return config.userProvidedTemplates().generation().enabled()
                || devServicesConfig.userProvidedTemplates().installation().enabled();
    }
}
