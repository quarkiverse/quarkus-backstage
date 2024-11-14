package io.quarkiverse.backstage.common.template;

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

import org.jboss.logging.Logger;

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

    public TemplateGenerator(Path projectDirPath, String name, String namespace) {
        this(projectDirPath, name, namespace, Optional.empty(), Optional.empty(), Optional.empty(), Collections.emptyList(),
                Optional.empty(), false, false, false);
    }

    public TemplateGenerator(Path projectDirPath, String name, String namespace, Optional<String> repositoryHost,
            Optional<Path> argoDirectoryPath, Optional<Path> helmDirectoryPath, List<Path> additionalFiles,
            Optional<EntityList> entityList,
            boolean exposeHealthEndpoint,
            boolean exposeMetricsEndpoint,
            boolean exposeInfoEndpoint) {
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
        Map<String, String> parameters = new HashMap<>();

        // Values that will be used as is when rendering the template
        // These are mostly references to parameters
        Map<String, String> templateValues = new HashMap<>();
        parameters.putAll(Projects.getProjectInfo(projectDirPath));
        parameters.put("componentId", parameters.getOrDefault("artifactId", name));

        templateValues.put("repoHost", "${{ parameters.repo.host }}");
        templateValues.put("repoOrg", "${{ parameters.repo.org }}");
        templateValues.put("repoName", "${{ parameters.repo.name }}");
        templateValues.put("repoBranch", "${{ parameters.repo.branch }}");

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
                                "The ID of the software component. This will be used as the name of the git repository and component.")
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
                        .withDefaultValue("code-with-quarkus")
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

        visitors.add(new AddRegisterComponentStep("register", isDevTemplate));

        String description = "Generated template from " + name + " application";
        if (isDevTemplate) {
            description += "This template is tuned for dev-mode instead of live ones.";
        }

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
        Path backstageDir = projectDirPath.resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path templateDir = templatesDir.resolve(templateName);

        Path skeletonDir = templateDir.resolve("skeleton");
        Path templateYamlPath = templateDir.resolve("template.yaml");

        Map<Path, String> content = new HashMap<>();
        content.put(templateYamlPath, templateContent);

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

        EntityList entityList = Serialization.unmarshalAsList(catalogInfoContent);
        // Recreate the catalog info using the proper source location
        entityList = new EntityListBuilder(entityList)
                .accept(new ApplyComponentAnnotation("backstage.io/source-location",
                        "url:" + (isDevTemplate ? "http://" : "https://")
                                + "${{ values.repoHost }}/${{ values.repoOrg }}/${{ values.repoName }}"))
                .build();

        content.put(catalogInfoPathInSkeleton, Serialization.asYaml(entityList));
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

        content.putAll(createSkeletonContent(skeletonDir, pomXmlPath, parameters, mavenEndpointTrasformer));
        content.putAll(createSkeletonContent(skeletonDir, buildGradlePath, parameters, gradleEndpointTrasformer));
        content.putAll(createSkeletonContent(skeletonDir, buildGradleKtsPath, parameters, gradleKtsEndpointTrasformer));

        if (argoDirectoryPath.isPresent()) {
            Path p = argoDirectoryPath.get();
            Path absoluteArgoDirectoryPath = p.isAbsolute() ? p : projectDirPath.resolve(p).toAbsolutePath();
            content.putAll(createSkeletonContent(skeletonDir, absoluteArgoDirectoryPath, parameters));
        }

        if (helmDirectoryPath.isPresent()) {
            Path p = helmDirectoryPath.get();
            Path absoluteHelmDirectoryPath = p.isAbsolute() ? p : projectDirPath.resolve(p).toAbsolutePath();
            content.putAll(createSkeletonContent(skeletonDir, absoluteHelmDirectoryPath, parameters));
        }

        for (Path additionalFile : additionalFiles) {
            content.putAll(createSkeletonContent(skeletonDir, additionalFile, parameters));
        }

        return content;
    }

    private Map<Path, String> createSkeletonContent(Path skeletonDir, Path path, Map<String, String> parameters) {
        return createSkeletonContent(skeletonDir, path, parameters, s -> s);
    }

    private Map<Path, String> createSkeletonContent(Path skeletonDir, Path path, Map<String, String> parameters,
            Function<String, String> transformer) {
        try {
            if (!path.toFile().exists()) {
                return Map.of();
            }

            if (path.toFile().isDirectory()) {
                return Files.walk(path)
                        .filter(Files::isRegularFile)
                        .map(p -> createSkeletonContent(skeletonDir, p, parameters))
                        .reduce(new HashMap<>(), (a, b) -> {
                            a.putAll(b);
                            return a;
                        });
            }

            String content = Files.readString(path);
            Path targetPath = toSkeletonPath(skeletonDir, path);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                content = parameterize(content, entry.getKey(), entry.getValue());
            }
            return Map.of(targetPath, transformer.apply(content));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    private Path toSkeletonPath(Path skeletonDir, Path file) {
        return skeletonDir.resolve(projectDirPath.relativize(file));
    }

    private String parameterize(String content, String name, String value) {
        if (value == null) {
            LOG.debugf("Value for %s is null. Ignoring.", value);
            return content;
        }
        String placeholder = "\\$\\{\\{ values\\." + name + " \\}\\}";
        return content.contains(value) ? content.replaceAll(Pattern.quote(value), placeholder) : content;
    }

    private void writeStringSafe(Path p, String content) {
        try {
            Files.writeString(p, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
