package io.quarkiverse.backstage.deployment;

import static io.quarkus.runtime.annotations.ConfigPhase.BUILD_TIME;

import java.util.Optional;

import com.aayushatharva.brotli4j.encoder.Encoder.Parameters;
import com.github.dockerjava.api.model.Endpoint;

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
     * Template configuration
     */
    CatalogConfiguration catalog();

    /**
     * Template configuration
     */
    TemplateConfiguration template();

    /**
     * Dev Template configuration
     */
    TemplateConfiguration devTemplate();

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

    interface CatalogConfiguration {
        /**
         * The generation configuration.
         */
        CatalogGeneration generation();
    }

    interface TemplateConfiguration {

        /**
         * The generation configuration.
         */
        TemplateGeneration generation();

        /**
         * The parameter configuration.
         */
        ParameterConfiguration parameters();

        /**
         * Template parameters configuration
         * Parameters parameters();
         *
         * /**
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

    interface CatalogGeneration {

        /**
         * Whether to enable the catalog-info.yaml generation at build time.
         */
        @WithDefault("true")
        boolean enabled();

    }

    interface TemplateGeneration {

        /**
         * Whether to enable template generation at build time.
         */
        @WithDefault("false")
        boolean enabled();

    }

    interface ParameterConfiguration {
        /**
         * Eddpoints configuration
         */
        Endpoints endpoints();
    }

    interface Endpoints {

        /**
         * Health endpoint configuration
         */
        Endpoint health();

        /**
         * Metrics endpoint configuration
         */
        Endpoint metrics();

        /**
         * Info endpoint configuration
         */
        Endpoint info();

    }

    interface Endpoint {
        /**
         * Whether to expose the endpoint as a template parameter
         */
        @WithDefault("false")
        boolean enabled();

    }
}
