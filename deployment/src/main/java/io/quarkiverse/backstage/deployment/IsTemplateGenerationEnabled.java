package io.quarkiverse.backstage.deployment;

import java.util.function.BooleanSupplier;

public class IsTemplateGenerationEnabled implements BooleanSupplier {

    BackstageConfiguration config;

    @Override
    public boolean getAsBoolean() {
        return config.template().generation().enabled();
    }

}
