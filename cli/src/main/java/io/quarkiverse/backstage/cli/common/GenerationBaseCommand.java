package io.quarkiverse.backstage.cli.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

import org.apache.maven.artifact.versioning.ComparableVersion;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.common.utils.Strings;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.bootstrap.BootstrapAppModelFactory;
import io.quarkus.bootstrap.BootstrapException;
import io.quarkus.bootstrap.app.AugmentAction;
import io.quarkus.bootstrap.app.CuratedApplication;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.builder.Version;
import io.quarkus.devtools.project.BuildTool;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.maven.dependency.ArtifactDependency;
import io.quarkus.maven.dependency.Dependency;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

public abstract class GenerationBaseCommand<T> extends BackstageClientAwareCommand implements Callable<Integer> {

    private static final ArtifactDependency QUARKUS_BACKSTAGE = new ArtifactDependency("io.quarkiverse.backstage",
            "quarkus-backstage", null, "jar", GenerationBaseCommand.getVersion());

    @Option(names = {
            "--namespace" }, description = "The target namespace (where the Backstage entities will be installed). Defaults to 'default'")
    protected Optional<String> namespace = Optional.empty();

    /**
     * The exit code of the command. Since {@link #process(Object, Path...)} doesn't return a value, subclasses
     * should set it when they abort.
     */
    protected int exitCode = ExitCode.OK;

    public GenerationBaseCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    public abstract void process(T obj, Path... additionalFiles);

    /**
     * Converts the payload passed by the handler (JDK types only) into the object expected by {@link #process}.
     */
    protected abstract T fromPayload(Object payload);

    public abstract String getHandlerName();

    public abstract String[] getRequiredBuildItems();

    public Properties getBuildSystemProperties() {
        Properties buildSystemProperties = new Properties();
        Path projectRoot = getWorkingDirectory();
        Path applicationPropertiesPath = projectRoot.resolve("src").resolve("main").resolve("resources")
                .resolve("application.properties");
        if (Files.exists(applicationPropertiesPath)) {
            try {
                buildSystemProperties.load(Files.newBufferedReader(applicationPropertiesPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        namespace.ifPresent(v -> buildSystemProperties.setProperty("quarkus.backstage.namespace", v));
        return buildSystemProperties;
    }

    public List<Dependency> getProjectDependencies() {
        List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(QUARKUS_BACKSTAGE);
        try {
            BootstrapAppModelFactory.newInstance()
                    .setProjectRoot(getWorkingDirectory())
                    .setLocalProjectsDiscovery(true)
                    .resolveAppModel()
                    .getApplicationModel()
                    .getDependencies().forEach(d -> {
                        dependencies.add(new ArtifactDependency(d.getGroupId(), d.getArtifactId(), d.getClassifier(),
                                d.getType(), d.getVersion()));
                    });
        } catch (BootstrapException e) {
            //Ignore, as it's currently broken for gradle
        }
        return dependencies;
    }

    public Integer call() {
        Path projectRoot = getWorkingDirectory();
        BuildTool buildTool = QuarkusProjectHelper.detectExistingBuildTool(projectRoot);
        Path targetDirecotry = projectRoot.resolve(buildTool.getBuildDirectory());
        List<Dependency> projectDependencies = getProjectDependencies();
        QuarkusBootstrap quarkusBootstrap = QuarkusBootstrap.builder()
                .setMode(QuarkusBootstrap.Mode.PROD)
                .setBuildSystemProperties(getBuildSystemProperties())
                .setApplicationRoot(getWorkingDirectory())
                .setProjectRoot(getWorkingDirectory())
                .setTargetDirectory(targetDirecotry)
                .setIsolateDeployment(isProjectQuarkusNewer(projectDependencies))
                .setRebuild(true)
                .setTest(false)
                .setLocalProjectDiscovery(true)
                .setBaseClassLoader(ClassLoader.getSystemClassLoader())
                .setForcedDependencies(projectDependencies)
                .build();

        // Checking
        try (CuratedApplication curatedApplication = quarkusBootstrap.bootstrap()) {
            AugmentAction action = curatedApplication.createAugmentor();

            // When the deployment is isolated, the handler runs with the project's Quarkus/extension classes:
            // only JDK types (BiConsumer, String, List, Map, Path) can cross the class loader boundary.
            BiConsumer<Object, Path[]> processor = (payload, paths) -> process(fromPayload(payload), paths);
            action.performCustomBuild(getHandlerName(), processor, getRequiredBuildItems());

        } catch (BootstrapException e) {
            throw new RuntimeException(e);
        }
        return exitCode;
    }

    /**
     * The build runs with the Quarkus classes bundled in the CLI, unless the project uses a newer Quarkus.
     * In that case the classes bundled in the CLI can't handle the resources of the project's Quarkus
     * (e.g. config roots and converters), so the deployment is isolated and uses the project's Quarkus instead.
     */
    static boolean isProjectQuarkusNewer(List<Dependency> projectDependencies) {
        return projectDependencies.stream()
                .filter(d -> "io.quarkus".equals(d.getGroupId()) && "quarkus-core".equals(d.getArtifactId()))
                .map(Dependency::getVersion)
                .anyMatch(v -> new ComparableVersion(v).compareTo(new ComparableVersion(Version.getVersion())) > 0);
    }

    public void saveCatalogInfo(EntityList entityList) {
        String catalogInfoContent = Serialization.asYaml(entityList);
        Path catalogInfoPath = getWorkingDirectory().resolve("catalog-info.yaml");
        writeStringSafe(catalogInfoPath, catalogInfoContent);
    }

    public Optional<String> getNamespace() {
        return namespace;
    }

    protected void writeStringSafe(Path p, String content) {
        try {
            Files.writeString(p, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Path getWorkingDirectory() {
        return Paths.get(System.getProperty("user.dir"));
    }

    private static String getVersion() {
        return Strings.read(GenerationBaseCommand.class.getClassLoader().getResourceAsStream("version"));
    }
}
