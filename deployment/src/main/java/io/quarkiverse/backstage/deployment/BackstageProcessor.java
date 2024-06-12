package io.quarkiverse.backstage.deployment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.quarkiverse.backstage.v1alpha1.ComponentBuilder;
import io.quarkus.deployment.IsTest;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.GeneratedFileSystemResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;

class BackstageProcessor {

    private static final String FEATURE = "backstage";
    private static final String DOT_GIT = ".git";
    private static final ObjectMapper YAML_MAPPER = new YAMLMapper().registerModule(new Jdk8Module())
            .setSerializationInclusion(Include.NON_EMPTY);

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIfNot = IsTest.class)
    public void build(ApplicationInfoBuildItem applicationInfo,
            OutputTargetBuildItem outputTargetBuildItem,
            BuildProducer<GeneratedFileSystemResourceBuildItem> generatedResourceProducer) {

        var component = new ComponentBuilder()
                .withNewMetadata()
                .withName(applicationInfo.getName())
                .endMetadata()
                .withNewSpec()
                .endSpec()
                .build();

        try {
            var str = YAML_MAPPER.writeValueAsString(component);
            generatedResourceProducer.produce(
                    new GeneratedFileSystemResourceBuildItem("../catalog-info.yaml", str.getBytes(StandardCharsets.UTF_8)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<Path> getScmRoot(OutputTargetBuildItem outputTargetBuildItem) {
        Path dir = outputTargetBuildItem.getOutputDirectory();
        while (dir != null && !dir.resolve(DOT_GIT).toFile().exists()) {
            dir = dir.getParent();
        }
        return Optional.ofNullable(dir).filter(p -> p.resolve(DOT_GIT).toFile().exists());
    }
}
