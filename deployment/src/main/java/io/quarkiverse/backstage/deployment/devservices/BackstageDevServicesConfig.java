package io.quarkiverse.backstage.deployment.devservices;

import java.util.Optional;
import java.util.OptionalInt;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.backstage.devservices")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface BackstageDevServicesConfig {

    /**
     * Enable the Backstage DevServices.
     */
    @WithDefault("false")
    boolean enabled();

    /**
     * The image to use for the Backstage container.
     **/
    @WithDefault("quay.io/iocanel/backstage")
    String image();

    /**
     * The exposed HTTP port for the Backstage container.
     * If not specified, it will pick a random port
     */
    OptionalInt httpPort();

    /**
     * The service to service token
     */
    @WithDefault("quarkus-backstage-token")
    String token();

    /**
     * The Github configuration.
     */
    Github github();

    interface Github {

        /**
         * The Github token to setup for Backstage.
         */
        Optional<String> token();
    }
}
