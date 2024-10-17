package io.quarkiverse.backstage.deployment;

import static io.quarkus.runtime.annotations.ConfigPhase.BUILD_TIME;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigRoot(phase = BUILD_TIME)
@ConfigMapping(prefix = "quarkus.backstage")
public interface BackstageConfiguration {

    /**
     *
     * Git repository configuration
     */
    GitConfiguration git();

    /**
     *
     * Template configuration
     */
    TemplateConfiguration template();

    interface GitConfiguration {

        /**
         * The URL of the Git repository to connect to.
         */
        Optional<String> url();

        /**
         * The remote of the Git repository to connect to.
         */
        @WithDefault("origin")
        String remote();

        /**
         * The branch of the Git repository to connect to.
         */
        @WithDefault("backstage")
        String branch();
    }

    interface TemplateConfiguration {

        /**
         * The generation configuration.
         */
        Generation generation();

        /**
         * The namespace of the template to generate.
         */
        @WithDefault("default")
        String namespace();

        /**
         * The name of the template to generate.
         */
        Optional<String> name();

        /**
         * The path of the template to install.
         */
        @WithDefault(".backstage/templates")
        String path();
    }

    interface Generation {

        /**
         * Whether to generate the template at build time.
         */
        @WithDefault("false")
        boolean enabled();

    }
}
