package io.quarkiverse.backstage.common.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkiverse.backstage.common.utils.*;
import io.quarkiverse.backstage.common.visitors.*;
import io.quarkiverse.backstage.common.visitors.component.*;
import io.quarkiverse.backstage.common.visitors.template.*;
import io.quarkiverse.backstage.model.builder.Visitor;
import io.quarkiverse.backstage.scaffolder.v1beta3.Property;
import io.quarkiverse.backstage.scaffolder.v1beta3.PropertyBuilder;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.quarkiverse.backstage.scaffolder.v1beta3.TemplateBuilder;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkiverse.backstage.v1alpha1.EntityListBuilder;

public class TemplateGenerator {

    private static final Logger LOG = Logger.getLogger(TemplateGenerator.class);

    private Path projectDirPath;
    private String name;
    private String namespace;
    private Optional<String> repositoryHost = Optional.empty();
    private Optional<Path> argoDirectoryPath = Optional.empty();
    private Optional<Path> helmDirectoryPath = Optional.empty();
    private Optional<EntityList> entityList;
    private List<Path> additionalFiles = new ArrayList<>();

    private boolean exposeHealthEndpoint;
    private boolean exposeMetricsEndpoint;
    private boolean exposeInfoEndpoint;

    private boolean exposeHelmValues;

    private boolean argoCdStepEnabled;
    private boolean argoCdConfigExposed;
    private Optional<String> argoCdPath;
    private Optional<String> argoCdNamespace;
    private Optional<String> argoCdInstance;
    private Optional<String> argoCdDestinationNamespace;

    public TemplateGenerator(Path projectDirPath, String name, String namespace) {
        this(projectDirPath, name, namespace, Optional.empty(), Optional.empty(), Optional.empty(), Collections.emptyList(),
                Optional.empty(), false, false, false, true, true, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());
    }

    public TemplateGenerator(Path projectDirPath, String name, String namespace, Optional<String> repositoryHost,
            Optional<Path> argoDirectoryPath, Optional<Path> helmDirectoryPath, List<Path> additionalFiles,
            Optional<EntityList> entityList,
            boolean exposeHealthEndpoint,
            boolean exposeMetricsEndpoint,
            boolean exposeInfoEndpoint,
            boolean exposeHelmValues,
            boolean argoCdStepEnabled,
            Optional<String> argoCdPath,
            Optional<String> argoCdNamespace,
            Optional<String> argoCDDestinationNamespace,
            Optional<String> argoCdInstance) {

        this.projectDirPath = projectDirPath;
        this.name = name;
        this.namespace = namespace;
        this.repositoryHost = repositoryHost;
        this.argoDirectoryPath = argoDirectoryPath;
        this.helmDirectoryPath = helmDirectoryPath;
        this.additionalFiles = additionalFiles;
        this.entityList = entityList;

        for (Path additionalFile : additionalFiles) {
            checkCommonRoot(projectDirPath, additionalFile);
        }

        this.exposeHealthEndpoint = exposeHealthEndpoint;
        this.exposeMetricsEndpoint = exposeMetricsEndpoint;
        this.exposeInfoEndpoint = exposeInfoEndpoint;
        this.exposeHelmValues = exposeHelmValues;

        this.argoCdStepEnabled = argoCdStepEnabled;
        this.argoCdPath = argoCdPath;
        this.argoCdNamespace = argoCdNamespace;
        this.argoCdDestinationNamespace = argoCDDestinationNamespace;
        this.argoCdInstance = argoCdInstance;
    }

    private static void checkCommonRoot(Path projectDirPath, Path candidate) {
        if (!candidate.startsWith(projectDirPath)) {
            throw new IllegalArgumentException(
                    "The path " + candidate + " is not under the project directory " + projectDirPath);
        }
    }

    public TemplateGenerator withRepositoryHost(String repositoryHost) {
        this.repositoryHost = Optional.of(repositoryHost);
        return this;
    }

    public TemplateGenerator withArgoDirectory(Path argoDirectoryPath) {
        this.argoDirectoryPath = Optional.of(argoDirectoryPath);
        return this;
    }

    public TemplateGenerator withHelmDirectory(Path helmDirectoryPath) {
        this.helmDirectoryPath = Optional.of(helmDirectoryPath);
        return this;
    }

    public TemplateGenerator withExposeHealthEndpoint(boolean exposeHealthEndpoint) {
        this.exposeHealthEndpoint = exposeHealthEndpoint;
        return this;
    }

    public TemplateGenerator withExposeMetricsEndpoint(boolean exposeMetricsEndpoint) {
        this.exposeMetricsEndpoint = exposeMetricsEndpoint;
        return this;
    }

    public TemplateGenerator withExposeInfoEndpoint(boolean exposeInfoEndpoint) {
        this.exposeInfoEndpoint = exposeInfoEndpoint;
        return this;
    }

    public TemplateGenerator withExposeHelmValues(boolean exposeHelmValues) {
        this.exposeHelmValues = exposeHelmValues;
        return this;
    }

    public TemplateGenerator withArgoCdStepEnabled(boolean argoCdStepEnabled) {
        this.argoCdStepEnabled = argoCdStepEnabled;
        return this;
    }

    public TemplateGenerator withArgoCdConfigExposed(boolean argoCdConfigExposed) {
        this.argoCdConfigExposed = argoCdConfigExposed;
        return this;
    }

    public TemplateGenerator withArgoCdPath(Optional<String> argoCdPath) {
        this.argoCdPath = argoCdPath;
        return this;
    }

    public TemplateGenerator withArgoCdPath(String argoCdPath) {
        this.argoCdPath = Optional.of(argoCdPath);
        return this;
    }

    public TemplateGenerator withArgoCdInstance(Optional<String> argoCdInstance) {
        this.argoCdInstance = argoCdInstance;
        return this;
    }

    public TemplateGenerator withArgoCdInstance(String argoCdInstance) {
        this.argoCdInstance = Optional.of(argoCdInstance);
        return this;
    }

    public TemplateGenerator withArgoCdNamespace(Optional<String> argoCdNamespace) {
        this.argoCdNamespace = argoCdNamespace;
        return this;
    }

    public TemplateGenerator withArgoCdNamespace(String argoCdNamespace) {
        this.argoCdNamespace = Optional.of(argoCdNamespace);
        return this;
    }

    public TemplateGenerator withArgoCdDestinationNamespace(Optional<String> argoCdDestinationNamespace) {
        this.argoCdDestinationNamespace = argoCdDestinationNamespace;
        return this;
    }

    public TemplateGenerator withArgoCdDestinationNamespace(String argoCdDestinationNamespace) {
        this.argoCdDestinationNamespace = Optional.of(argoCdDestinationNamespace);
        return this;
    }

    public TemplateGenerator withAdditionalFiles(List<Path> additionalFiles) {
        for (Path additionalFile : additionalFiles) {
            checkCommonRoot(projectDirPath, additionalFile);
        }
        this.additionalFiles = additionalFiles;
        return this;
    }

    public TemplateGenerator addNewFile(Path file) {
        checkCommonRoot(projectDirPath, file);
        this.additionalFiles.add(file);
        return this;
    }

    public TemplateGenerator withEntityList(EntityList entityList) {
        this.entityList = Optional.of(entityList);
        return this;
    }

    public TemplateGenerator withEntityList(Optional<EntityList> entityList) {
        this.entityList = entityList;
        return this;
    }

    public TemplateGenerator addNewEntity(Entity entity) {
        if (this.entityList.isEmpty()) {
            this.entityList = Optional.of(new EntityListBuilder().build());
        }
        this.entityList.get().getItems().add(entity);
        return this;
    }

    public Map<Path, String> generate() {
        return generate(false);
    }

    private Map<Path, String> generate(boolean isDevTemplate) {
        Optional<String> basePackage = Packages.findCommonPackagePrefix(projectDirPath);

        // Things that will be parameterized
        // For examples when using: x -> my-app, then all references of my app will be replaced by ${{ parameters.x }}.
        Map<String, String> parameters = new LinkedHashMap<>();

        // Values that will be used as is when rendering the template
        // These are mostly references to parameters
        Map<String, String> templateValues = new HashMap<>();
        parameters.put("componentId", parameters.getOrDefault("artifactId", name));

        parameters.putAll(Projects.getProjectInfo(projectDirPath));

        templateValues.put("repoHost", "${{ parameters.repo.host }}");
        templateValues.put("repoOrg", "${{ parameters.repo.org }}");
        templateValues.put("repoName", "${{ parameters.repo.name }}");
        templateValues.put("repoBranch", "${{ parameters.repo.branch }}");
        templateValues.put("repoUrl",
                "https://${{ parameters.repo.host }}/${{ parameters.repo.org }}/${{ parameters.repo.name }}.git");

        if (exposeMetricsEndpoint) {
            templateValues.put("metricsEndpoint", "${{ parameters.metricsEndpoint }}");
        }
        if (exposeHealthEndpoint) {
            templateValues.put("healthEndpoint", "${{ parameters.healthEndpoint }}");
        }
        if (exposeInfoEndpoint) {
            templateValues.put("infoEndpoint", "${{ parameters.infoEndpoint }}");
        }

        basePackage.ifPresent(p -> parameters.put("package", p));

        String templateName = isDevTemplate ? name + "-dev" : name;
        List<Visitor> visitors = new ArrayList<>();

        visitors.add(new AddNewEntityRefToOutput("Open the catalog info", "${{ steps.register.output.entityRef }}"));
        visitors.add(new AddNewUrlToOutput("Open the repository",
                "https://${{ values.repoHost }}/${{ values.repoOrg }}/${{ values.repoName }}"));

        visitors.add(new AddNewTemplateParameter("Component configuration",
                new PropertyBuilder()
                        .withName("componentId")
                        .withTitle("Component ID")
                        .withDescription(
                                "The ID of the software component. This will be used as the name of the git repository, component.")
                        .withType("string")
                        .withDefaultValue("my-app")
                        .withRequired(true)
                        .build()));

        visitors.add(new AddNewTemplateParameter("Project details",
                new PropertyBuilder()
                        .withName("groupId")
                        .withTitle("Group ID")
                        .withDescription("The group ID of the project")
                        .withType("string")
                        .withDefaultValue("org.acme")
                        .withRequired(true)
                        .build(),

                new PropertyBuilder()
                        .withName("artifactId")
                        .withTitle("Artifact ID")
                        .withDescription("The artifact ID of the project")
                        .withType("string")
                        .withDefaultValue("my-app")
                        .withRequired(true)
                        .build(),

                new PropertyBuilder()
                        .withName("version")
                        .withTitle("Version")
                        .withDescription("The version of the project")
                        .withType("string")
                        .withDefaultValue("1.0-SNAPSHOT")
                        .withRequired(true)
                        .build(),

                new PropertyBuilder()
                        .withName("name")
                        .withTitle("Name")
                        .withDescription("The name of the project")
                        .withType("string")
                        .build(),

                new PropertyBuilder()
                        .withName("description")
                        .withTitle("Description")
                        .withDescription("The description of the project")
                        .withType("string")
                        .build(),

                new PropertyBuilder()
                        .withName("package")
                        .withTitle("Package")
                        .withDescription("The base package of the project")
                        .withType("string")
                        .withDefaultValue("org.acme")
                        .build()

        ));

        List<Property> endpointProperties = new ArrayList<>();
        if (exposeHealthEndpoint) {
            endpointProperties.add(new PropertyBuilder()
                    .withName("healthEndpoint")
                    .withTitle("Health Endpoint")
                    .withDescription("Whether to expose the health endpoint")
                    .withType("boolean")
                    .build());
        }

        if (exposeMetricsEndpoint) {
            endpointProperties.add(new PropertyBuilder()
                    .withName("metricsEndpoint")
                    .withTitle("Metrics Endpoint")
                    .withDescription("Whether to expose the metrics endpoint")
                    .withType("boolean")
                    .build());
        }

        if (exposeInfoEndpoint) {
            endpointProperties.add(new PropertyBuilder()
                    .withName("infoEndpoint")
                    .withTitle("Info Endpoint")
                    .withDescription("Whether to expose the info endpoint")
                    .withType("boolean")
                    .build());
        }

        if (!endpointProperties.isEmpty()) {
            visitors.add(new AddNewTemplateParameter("Endpoint configuration",
                    endpointProperties.toArray(new Property[endpointProperties.size()])));
        }

        Map<String, Property> repoProperties = new LinkedHashMap<>();
        if (isDevTemplate) {
            repoProperties.put("host", new PropertyBuilder()
                    .withName("host")
                    .withTitle("Host")
                    .withDescription("The host of the git repository")
                    .withType("string")
                    .withDefaultValue(repositoryHost.orElse("gitea:3000"))
                    .withRequired(true)
                    .build());
        } else {
            repoProperties.put("host", new PropertyBuilder()
                    .withName("host")
                    .withTitle("Host")
                    .withDescription("The host of the git repository")
                    .withType("string")
                    .withDefaultValue("github.com")
                    .withRequired(true)
                    .build());
        }

        repoProperties.put("org", new PropertyBuilder()
                .withName("org")
                .withTitle("Organization")
                .withDescription("The organization of the git repository")
                .withType("string")
                .withDefaultValue("dev")
                .withRequired(true)
                .build());

        repoProperties.put("name", new PropertyBuilder()
                .withName("name")
                .withTitle("Name")
                .withDescription("The name of the git repository")
                .withType("string")
                .withDefaultValue("my-app")
                .withRequired(true)
                .build());

        repoProperties.put("branch", new PropertyBuilder()
                .withName("branch")
                .withTitle("Branch")
                .withDescription("The branch of the git repository")
                .withType("string")
                .withDefaultValue("main")
                .withRequired(true)
                .build());

        repoProperties.put("visibility", new PropertyBuilder()
                .withName("visibility")
                .withTitle("Visibility")
                .withDescription("The visibility of the git repository")
                .withType("string")
                .withDefaultValue("public")
                .withRequired(true)
                .build());

        visitors.add(new AddNewTemplateParameter("Git repository configuration",
                new PropertyBuilder()
                        .withName("repo")
                        .withType("object")
                        .withTitle("Repository Configuration")
                        .withRequired(true)
                        .withProperties(repoProperties)
                        .build()));

        visitors.add(new ApplyMetadataTag("java"));
        visitors.add(new ApplyMetadataTag("quarkus"));
        if (isDevTemplate) {
            visitors.add(new ApplyMetadataTag("dev"));
        }

        //Render the app skeleton
        visitors.add(new AddNewFetchTemplateStep("render", "skeleton/", true, List.of(), parameters, templateValues));
        if (isDevTemplate) {
            visitors.add(new AddPublishGiteaStep("publish"));
            visitors.add(new ApplyMetadataAnnotation("backstage.io/child-of", name));
        } else {
            visitors.add(new AddPublishGithubStep("publish"));
        }

        String description = "Generated template from " + name + " application";
        if (isDevTemplate) {
            description += "This template is tuned for dev-mode instead of live ones.";
        }

        Path backstageDir = projectDirPath.resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path templateDir = templatesDir.resolve(templateName);

        Path skeletonDir = templateDir.resolve("skeleton");
        Path templateYamlPath = templateDir.resolve("template.yaml");

        Map<Path, String> content = new HashMap<>();

        this.entityList.ifPresent(list -> {
            Path catalogInfoPath = projectDirPath.resolve("catalog-info.yaml");
            try {
                Files.write(catalogInfoPath, Serialization.asYaml(list).getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to write catalog-info.yaml", e);
            }
        });

        Path srcMainJavaDir = projectDirPath.resolve("src").resolve("main").resolve("java");
        Path srcMainResourcesDir = projectDirPath.resolve("src").resolve("main").resolve("resources");
        Path srcTestJavaDir = projectDirPath.resolve("src").resolve("test").resolve("java");
        Path srcTestResourcesDir = projectDirPath.resolve("src").resolve("test").resolve("resources");

        Path destMainJavaBase = skeletonDir.resolve("src").resolve("main").resolve("java").resolve("${{ values.package }}");
        Path destMainResourcesBase = skeletonDir.resolve("src").resolve("main").resolve("resources");
        Path destTestJavaBase = skeletonDir.resolve("src").resolve("test").resolve("java").resolve("${{ values.package }}");
        Path destTestResourcesBase = skeletonDir.resolve("src").resolve("test").resolve("resources");

        content.putAll(
                SourceTransformer.transform(srcMainJavaDir, destMainJavaBase.getParent(), parameters, basePackage.orElse("")));
        content.putAll(SourceTransformer.transform(srcMainResourcesDir, destMainResourcesBase.getParent(), parameters,
                basePackage.orElse("")));
        content.putAll(
                SourceTransformer.transform(srcTestJavaDir, destTestJavaBase.getParent(), parameters, basePackage.orElse("")));
        content.putAll(SourceTransformer.transform(srcTestResourcesDir, destTestResourcesBase.getParent(), parameters,
                basePackage.orElse("")));

        Path catalogInfoPath = projectDirPath.resolve("catalog-info.yaml");
        Map<Path, String> catalogInfoPathToContent = createSkeletonContent(skeletonDir, catalogInfoPath, parameters);

        Path catalogInfoPathInSkeleton = catalogInfoPathToContent.keySet().iterator().next();
        String catalogInfoContent = catalogInfoPathToContent.values().iterator().next();

        List<Visitor<?>> catalogInfoVisitors = new ArrayList<>();
        catalogInfoVisitors.add(new ApplyComponentAnnotation("backstage.io/source-location",
                "url:" + (isDevTemplate ? "http://" : "https://")
                        + "${{ values.repoHost }}/${{ values.repoOrg }}/${{ values.repoName }}"));

        // We shall rerender the catalog-info.yaml later, to give time to ohter parts of the code to contribute visitors to it.
        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("README.md"), parameters));
        content.putAll(createSkeletonContent(skeletonDir, projectDirPath.resolve("readme.md"), parameters));

        Path pomXmlPath = projectDirPath.resolve("pom.xml");
        Path buildGradlePath = projectDirPath.resolve("build.gradle");
        Path buildGradleKtsPath = projectDirPath.resolve("build.gradle.kts");

        // Maven transformation functions
        Function<String, String> mavenHealthEndpointTransformer = s -> exposeHealthEndpoint
                ? Maven.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-smallrye-health", Optional.empty())
                : s;
        Function<String, String> mavenMetricsEndpointTransformer = s -> exposeMetricsEndpoint
                ? Maven.addOptionalDependency(s, "metricsEndpoint", "io.quarkus", "quarkus-micrometer", Optional.empty())
                : s;
        Function<String, String> mavenMetricsRegistryPrometheusEndpointTransformer = s -> exposeMetricsEndpoint
                ? Maven.addOptionalDependency(s, "metricsEndpoint", "io.quarkus", "quarkus-micrometer-registry-prometheus",
                        Optional.empty())
                : s;
        Function<String, String> mavenInfoEndpointTransformer = s -> exposeInfoEndpoint
                ? Maven.addOptionalDependency(s, "infoEndpoint", "io.quarkus", "quarkus-info", Optional.empty())
                : s;
        Function<String, String> mavenEndpointTrasformer = mavenHealthEndpointTransformer
                .andThen(mavenMetricsEndpointTransformer).andThen(mavenMetricsRegistryPrometheusEndpointTransformer)
                .andThen(mavenInfoEndpointTransformer);

        // Gradle transformation functions
        Function<String, String> gradleHealthEndpointTransformer = s -> exposeHealthEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-smallrye-health", Optional.empty())
                : s;
        Function<String, String> gradleMetricsEndpointTransformer = s -> exposeMetricsEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-micrometer", Optional.empty())
                : s;
        Function<String, String> gradleMetricsRegistryPrometheusEndpointTransformer = s -> exposeMetricsEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-micrometer-registry-prometheus",
                        Optional.empty())
                : s;
        Function<String, String> gradleInfoEndpointTransformer = s -> exposeInfoEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-info", Optional.empty())
                : s;
        Function<String, String> gradleEndpointTrasformer = gradleHealthEndpointTransformer
                .andThen(gradleMetricsEndpointTransformer).andThen(gradleMetricsRegistryPrometheusEndpointTransformer)
                .andThen(gradleInfoEndpointTransformer);

        //Gradle Kotlin transformation functions
        Function<String, String> gradleKtsHealthEndpointTransformer = s -> exposeHealthEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-smallrye-health", Optional.empty())
                : s;
        Function<String, String> gradleKtsMetricsEndpointTransformer = s -> exposeMetricsEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-micrometer", Optional.empty())
                : s;
        Function<String, String> gradleKtsMetricsRegistryPrometheusEndpointTransformer = s -> exposeMetricsEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-micrometer-registry-prometheus",
                        Optional.empty())
                : s;
        Function<String, String> gradleKtsInfoEndpointTransformer = s -> exposeInfoEndpoint
                ? Gradle.addOptionalDependency(s, "healhtEndpoint", "io.quarkus", "quarkus-info", Optional.empty())
                : s;
        Function<String, String> gradleKtsEndpointTrasformer = gradleKtsHealthEndpointTransformer
                .andThen(gradleKtsMetricsEndpointTransformer).andThen(gradleKtsMetricsRegistryPrometheusEndpointTransformer)
                .andThen(gradleKtsInfoEndpointTransformer);

        content.putAll(createSkeletonContent(skeletonDir, buildGradlePath, parameters, gradleEndpointTrasformer));
        content.putAll(createSkeletonContent(skeletonDir, buildGradleKtsPath, parameters, gradleKtsEndpointTrasformer));

        if (argoDirectoryPath.isPresent()) {
            Path p = argoDirectoryPath.get();
            Path absoluteArgoDirectoryPath = p.isAbsolute() ? p : projectDirPath.resolve(p).toAbsolutePath();

            Map<String, String> argoParameters = new LinkedHashMap<>();
            ArgoCD.getRepositoryUrl(p, name).ifPresent(repoUrl -> {
                argoParameters.put("repoUrl", repoUrl);
            });
            argoParameters.putAll(parameters);

            Map<Path, String> argoContent = new HashMap<>();
            argoContent.putAll(createSkeletonContent(skeletonDir, absoluteArgoDirectoryPath, argoParameters));

            templateValues.put("argoCDNamespace", "${{ parameters.argocd.namespace }}");
            templateValues.put("argoCDDestinationNamespace", "${{ parameters.argocd.destination.namespace }}");

            content.putAll(ArgoCD.parameterize(argoContent, argoParameters));
            if (argoCdStepEnabled) {
                if (argoCdConfigExposed) {
                    visitors.add(new AddArgoCDCreateResourcesStep("deploy", true));
                    visitors.add(new AddNewTemplateParameter("ArgoCD Configuration",
                            new PropertyBuilder()
                                    .withName("argocd")
                                    .withType("object")
                                    .withProperties(
                                            Map.of(
                                                    "path",
                                                    new PropertyBuilder()
                                                            .withName("path")
                                                            .withType("string")
                                                            .withDefaultValue(".argocd/")
                                                            .build(),
                                                    "instance",
                                                    new PropertyBuilder()
                                                            .withName("instance")
                                                            .withType("string")
                                                            .withDefaultValue(argoCdInstance)
                                                            .build(),
                                                    "namespace",
                                                    new PropertyBuilder()
                                                            .withName("namespace")
                                                            .withType("string")
                                                            .withDefaultValue(argoCdNamespace)
                                                            .build(),
                                                    "destination",
                                                    new PropertyBuilder()
                                                            .withName("destination")
                                                            .withType("object")
                                                            .withProperties(
                                                                    Map.of(
                                                                            "namespace",
                                                                            new PropertyBuilder()
                                                                                    .withName("namespace")
                                                                                    .withType("string")
                                                                                    .withDefaultValue(
                                                                                            argoCdDestinationNamespace)
                                                                                    .build()))
                                                            .build()))
                                    .build()));
                } else {
                    visitors.add(new AddArgoCDCreateResourcesStep("ci-cd", argoCdPath, argoCdInstance, argoCdNamespace,
                            argoCdDestinationNamespace));
                }
            }
            catalogInfoVisitors.add(new ApplyComponentAnnotation("argocd/app-name", "${{ values.componentId }}"));
            catalogInfoVisitors.add(new ApplyComponentAnnotation("backstage.io/kubernetes-id", "${{ values.componentId }}"));
        }

        if (helmDirectoryPath.map(Path::toFile).filter(File::exists).isPresent()) {
            Path p = helmDirectoryPath.get();
            Path absoluteHelmDirectoryPath = p.isAbsolute() ? p : projectDirPath.resolve(p).toAbsolutePath();
            Path kubernetesDir = absoluteHelmDirectoryPath.resolve("kubernetes");
            Path openshiftDir = absoluteHelmDirectoryPath.resolve("openshift");
            Path chartContainerDir = kubernetesDir.toFile().exists() ? kubernetesDir : openshiftDir;

            if (chartContainerDir.toFile().isDirectory()) {
                Map<Path, String> helmContent = new HashMap<>();
                helmContent.putAll(createSkeletonContent(skeletonDir, absoluteHelmDirectoryPath, parameters));

                if (exposeHelmValues) {
                    Map<String, Property> properties = new HashMap<>();
                    for (File helmChartPath : chartContainerDir.toFile().listFiles()) {
                        if (helmChartPath.isDirectory()) {
                            Path valuesYamlPath = helmChartPath.toPath().resolve("values.yaml");
                            Map<String, Object> valuesMap = Serialization.unmarshal(valuesYamlPath.toFile(),
                                    new TypeReference<Map<String, Object>>() {
                                    });
                            properties.put("helm", new PropertyBuilder()
                                    .withName("helm")
                                    .withType("object")
                                    .withProperties(toProperties(valuesMap))
                                    .build());

                            Map<String, Object> parameterizedValues = Helm.parameterize(valuesMap);
                            Path targetValuesYamlPath = parameterize(valuesYamlPath, skeletonDir, parameters).keySet().stream()
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Failed to parameterize: " + valuesYamlPath));
                            String parameterizedValuesYaml = Serialization.asYaml(parameterizedValues);
                            helmContent.put(targetValuesYamlPath, parameterizedValuesYaml);
                        }
                    }
                    visitors.add(new AddNewTemplateParameter("Helm values",
                            properties.values().toArray(new Property[properties.size()])));
                }
                if (argoCdStepEnabled) {
                    Helm.listValuesYamlPaths(helmContent).forEach(path -> {
                        String valuesYamlContent = helmContent.get(path);
                        Map<String, Object> valuesMap = Serialization.unmarshal(valuesYamlContent,
                                new TypeReference<Map<String, Object>>() {
                                });
                        if (valuesMap.get("app") instanceof Map app) {
                            app.put("namespace", "${{ values.argoCDDestinationNamespace }}");
                        }
                        helmContent.put(path, Serialization.asYaml(valuesMap));
                    });

                    Helm.listTemplatePaths(helmContent).forEach(path -> {
                        String chartContent = helmContent.get(path);
                        chartContent = Namespaces.addNamespace(chartContent, "{{ .Values.app.namespace }}");
                        helmContent.put(path, chartContent);
                    });
                }
                content.putAll(Helm.parameterize(helmContent, parameters));
            }
        }

        for (Path additionalFile : additionalFiles) {
            content.putAll(createSkeletonContent(skeletonDir, additionalFile, parameters));
        }

        EntityList entityList = Serialization.unmarshalAsList(catalogInfoContent);
        // Recreate the catalog info using the proper source location
        entityList = new EntityListBuilder(entityList)
                .accept(catalogInfoVisitors.toArray(new Visitor[catalogInfoVisitors.size()]))
                .build();

        content.put(catalogInfoPathInSkeleton, Serialization.asYaml(entityList));

        visitors.add(new AddRegisterComponentStep("register", isDevTemplate));
        Template template = new TemplateBuilder()
                .withNewMetadata()
                .withName(templateName)
                .withDescription(Optional.of(description))
                .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                .withType("service")
                .withNewOutput()
                .endOutput()
                .endSpec()
                .accept(visitors.toArray(new Visitor[visitors.size()]))
                .build();

        String templateContent = Serialization.asYaml(template);
        content.put(templateYamlPath, templateContent);

        return content;
    }

    /**
     * Projects the specified file into the skeleton directory.
     * The method gets the relative path of the file from the project directory.
     * Then it projects the relative path into the destination directory.
     *
     * @param file the file to project
     * @param destinationDir the destination directory
     * @return file under the destination directory.
     */
    private Path project(Path file, Path destinationDir) {
        return destinationDir.resolve(projectDirPath.relativize(file));
    }

    private Map<Path, String> createSkeletonContent(Path skeletonDir, Path path, Map<String, String> parameters) {
        return createSkeletonContent(skeletonDir, path, parameters, s -> s);
    }

    private Map<Path, String> createSkeletonContent(Path skeletonDir, Path path, Map<String, String> parameters,
            Function<String, String> transformer) {
        try {
            if (!Files.exists(path)) {
                return Map.of();
            }

            if (Files.isDirectory(path)) {
                return Files.walk(path)
                        .filter(Files::isRegularFile)
                        .map(p -> createSkeletonContent(skeletonDir, p, parameters))
                        .flatMap(m -> m.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            String content = Files.readString(path);
            Path targetPath = project(path, skeletonDir);
            return Map.of(targetPath, transformer.apply(parameterize(content, parameters)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    /**
     * Parameterize the source and project it into the destination directory.
     * If source is a directory, then walk the directory and parameterize each file.
     *
     * @param source the source file or directory
     * @param destinationDir the destination directory
     * @param parameters the parameters
     * @return the parameterized content
     */
    private Map<Path, String> parameterize(Path source, Path destinationDir, Map<String, String> parameters) {
        return parameterize(source, destinationDir, parameters, s -> s);
    }

    private Map<Path, String> parameterize(Path source, Path destinationDir, Map<String, String> parameters,
            Function<String, String> transformer) {
        try {
            if (!Files.exists(source)) {
                return Map.of();
            }

            if (Files.isDirectory(source)) {
                return Files.walk(source)
                        .filter(Files::isRegularFile)
                        .map(p -> parameterize(p, destinationDir, parameters))
                        .flatMap(m -> m.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            String content = Files.readString(source);
            Path targetPath = project(source, destinationDir);
            return Map.of(targetPath, transformer.apply(parameterize(content, parameters)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + source, e);
        }
    }

    /**
     * De-interpolates the specified content with the given parameters.
     * It replaces values found in the map with placeholders derived from the keys.
     * Example:
     * content = "The http port is 8080"
     * parameters = {"port": "8080", protocol": "http"}
     * returns "The ${{ values.protocol }} port is ${{ values.port }}"
     *
     * @param content the content to parameterize
     * @param parameters the parameters
     * @return the parameterized content
     */
    private String parameterize(String content, Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            content = parameterize(content, entry.getKey(), entry.getValue());
        }
        return content;
    }

    /**
     * Replaces the specified value with a named placeholder.
     * Example:
     * content = "The port is 8080"
     * name = "port"
     * value = "8080"
     * returns "The port is ${{ values.port }}"
     *
     * @param content the content to parameterize
     * @param name the name of the parameter
     * @param value the value of the parameter
     * @return the parameterized content
     */
    private String parameterize(String content, String name, String value) {
        if (value == null) {
            LOG.debugf("Value for %s is null. Ignoring.", value);
            return content;
        }
        String placeholder = "\\$\\{\\{ values\\." + name + " \\}\\}";
        return content.contains(value) ? content.replaceAll(Pattern.quote(value), placeholder) : content;
    }

    Map<String, Property> toProperties(Map<String, Object> parameters) {
        Map<String, Property> properties = new HashMap<>();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map m) {
                Map<String, Property> nestedProperties = toProperties(m);
                properties.put(key, new PropertyBuilder()
                        .withName(key)
                        .withType("object")
                        .withProperties(nestedProperties)
                        .build());

            } else {
                String type = "string";
                if (value instanceof Number) {
                    type = "number";
                } else if (value.getClass().isArray()) {
                    type = "array";
                }

                properties.put(key, new PropertyBuilder()
                        .withName(key)
                        .withType(type)
                        .withDefaultValue(value)
                        .build());
            }
        }
        return properties;
    }

    Map<String, String> toParams(Map<String, Object> parameters) {
        return toParams("", parameters);
    }

    Map<String, String> toParams(String prefix, Map<String, Object> parameters) {
        Map<String, String> properties = new HashMap<>();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map m) {
                String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;
                properties.putAll(toParams(newPrefix, m));
            } else {
                properties.put(prefix + "." + key, String.valueOf(value));
            }
        }
        return properties;
    }

    private void writeStringSafe(Path p, String content) {
        try {
            Files.writeString(p, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
