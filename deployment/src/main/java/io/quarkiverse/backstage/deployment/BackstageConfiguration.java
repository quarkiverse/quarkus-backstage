package io.quarkiverse.backstage.deployment;

import static io.quarkus.runtime.annotations.ConfigPhase.BUILD_TIME;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
     * Template configuration
     */
    CatalogConfiguration catalog();

    /**
     * Template configuration
     */
    TemplateConfiguration template();

    /**
     * User provided template(s) configuration
     */
    UserProvidedTemplateConfiguration userProvidedTemplates();

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
         * The steps configuration.
         */
        StepsConfiguration steps();

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

    interface UserProvidedTemplateConfiguration {

        Path FALLBACK_PATH = Paths.get("src", "main", "backstage", "templates");

        /**
         * The generation configuration for the user provided templates.
         */
        TemplateGeneration generation();

        /**
         * The path of the user provided template(s) to install.
         * Falls back to 'src/main/backstage/templates' if not provided.
         */
        Optional<String> path();

        /**
         * A List of URLs to download the user provided template(s) from.
         **/
        Optional<List<String>> urls();

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
         * Endpoints configuration
         */
        Endpoints endpoints();

        /**
         * Helm configuration
         */
        Helm helm();

        /**
         * ArgoCD Parameters Configuration
         */
        ArgoCDParameters argoCd();
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

    interface Helm {
        /**
         * Whether to expose helm values as parameters
         */
        @WithDefault("true")
        boolean enabled();
    }

    interface ArgoCDParameters {
        /**
         * Whether to expose ArgoCD configuration as parameters
         */
        @WithDefault("true")
        boolean enabled();
    }

    interface StepsConfiguration {
        /**
         * The generation configuration.
         */
        ArgoCD argoCd();
    }

    interface ArgoCD {
        /**
         * Whether to enable ArgoCD steps generation at build time.
         */
        @WithDefault("true")
        boolean enabled();

        /**
         * The path the ArgoCD are expected.
         */
        Optional<String> path();

        /**
         * The namespace the ArgoCD resources will be created in.
         */
        @WithDefault("default")
        String namespace();

        /**
         * The name of the ArgoCD instance.
         */
        @WithDefault("default")
        String instance();
    }
}
