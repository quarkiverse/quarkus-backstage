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
import java.util.function.Consumer;

import io.dekorate.utils.Strings;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.bootstrap.BootstrapAppModelFactory;
import io.quarkus.bootstrap.BootstrapException;
import io.quarkus.bootstrap.app.AugmentAction;
import io.quarkus.bootstrap.app.CuratedApplication;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.devtools.project.BuildTool;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.maven.dependency.ArtifactDependency;
import io.quarkus.maven.dependency.Dependency;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

public abstract class GenerationBaseCommand<T> extends BackstageClientAwareCommand implements Callable<Integer> {

    private static final ArtifactDependency QUARKUS_BACKSTAGE = new ArtifactDependency("io.quarkiverse.backstage",
            "quarkus-backstage", null, "jar", GenerationBaseCommand.getVersion());

    @Option(names = { "--namespace" }, description = "The target namespace (where the Custom Resources will be installed)")
    protected Optional<String> namespace = Optional.empty();

    public GenerationBaseCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    public abstract void process(T obj);

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
        QuarkusBootstrap quarkusBootstrap = QuarkusBootstrap.builder()
                .setMode(QuarkusBootstrap.Mode.PROD)
                .setBuildSystemProperties(getBuildSystemProperties())
                .setApplicationRoot(getWorkingDirectory())
                .setProjectRoot(getWorkingDirectory())
                .setTargetDirectory(targetDirecotry)
                .setIsolateDeployment(false)
                .setRebuild(true)
                .setLocalProjectDiscovery(true)
                .setBaseClassLoader(ClassLoader.getSystemClassLoader())
                .setForcedDependencies(getProjectDependencies())
                .build();

        // Checking
        try (CuratedApplication curatedApplication = quarkusBootstrap.bootstrap()) {
            AugmentAction action = curatedApplication.createAugmentor();

            action.performCustomBuild(getHandlerName(), new Consumer<T>() {
                @Override
                public void accept(T obj) {
                    process(obj);

                }
            }, getRequiredBuildItems());

        } catch (BootstrapException e) {
            throw new RuntimeException(e);
        }
        return ExitCode.OK;
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
