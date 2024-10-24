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
import io.quarkiverse.backstage.cli.handlers.GetBackstageEntitiesHandler;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.spi.EntityListBuildItem;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkus.bootstrap.BootstrapException;
import io.quarkus.bootstrap.app.AugmentAction;
import io.quarkus.bootstrap.app.CuratedApplication;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.devtools.project.BuildTool;
import io.quarkus.devtools.project.QuarkusProject;
import io.quarkus.devtools.project.QuarkusProjectHelper;
import io.quarkus.maven.dependency.ArtifactDependency;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

public abstract class GenerationBaseCommand extends EntityBaseCommand implements Callable<Integer> {

    private static final ArtifactDependency QUARKUS_BACKSTAGE = new ArtifactDependency("io.quarkiverse.backstage",
            "quarkus-backstage", null, "jar", GenerationBaseCommand.getVersion());

    @Option(names = { "--namespace" }, description = "The target namespace (where the Custom Resources will be installed)")
    protected Optional<String> namespace = Optional.empty();

    public GenerationBaseCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    public Properties getBuildSystemProperties() {
        Properties buildSystemProperties = new Properties();
        namespace.ifPresent(v -> buildSystemProperties.setProperty("quarkus.backstage.namespace", v));
        return buildSystemProperties;
    }

    public Integer call() {
        Path projectRoot = getWorkingDirectory();
        BuildTool buildTool = QuarkusProjectHelper.detectExistingBuildTool(projectRoot);
        Path targetDirecotry = projectRoot.resolve(buildTool.getBuildDirectory());
        QuarkusProject p;
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
                .setForcedDependencies(List.of(QUARKUS_BACKSTAGE))
                .build();

        List<String> resultBuildItemFQCNs = new ArrayList<>();
        resultBuildItemFQCNs.add(EntityListBuildItem.class.getName());

        // Checking
        try (CuratedApplication curatedApplication = quarkusBootstrap.bootstrap()) {
            AugmentAction action = curatedApplication.createAugmentor();

            action.performCustomBuild(GetBackstageEntitiesHandler.class.getName(), new Consumer<EntityList>() {
                @Override
                public void accept(EntityList entityList) {
                    if (entityList.getItems().isEmpty()) {
                        System.out.println("Can't generate backstage custom resources.");
                        return;
                    }

                    process(entityList);

                }
            }, resultBuildItemFQCNs.toArray(new String[resultBuildItemFQCNs.size()]));

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

    public abstract void process(EntityList entityList);

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
