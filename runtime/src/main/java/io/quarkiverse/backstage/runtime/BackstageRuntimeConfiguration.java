package io.quarkiverse.backstage.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "quarkus.backstage")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface BackstageRuntimeConfiguration {

    /**
     * The URL of the Backstage API
     */
    Optional<String> url();

    /**
     *
     * The token needed to authenticate with the Backstage API
     */
    Optional<String> token();
}
