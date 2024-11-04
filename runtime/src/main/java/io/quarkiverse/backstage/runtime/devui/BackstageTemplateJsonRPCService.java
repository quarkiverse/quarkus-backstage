package io.quarkiverse.backstage.runtime.devui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.dsl.Gitea;
import io.quarkiverse.backstage.common.template.TemplateGenerator;
import io.quarkiverse.backstage.v1alpha1.Location;

@ActivateRequestContext
public class BackstageTemplateJsonRPCService {

    private static final Logger LOG = Logger.getLogger(BackstageTemplateJsonRPCService.class);

    @Inject
    BackstageClient backstageClient;

    public String generate(String path, String name, String namespace) {
        return this.generate(path, name, namespace, null);
    }

    public String generate(String path, String name, String namespace, String giteaSharedNetworkUrl) {
        Path rootDir = Paths.get(path);
        String repositoryHost = giteaSharedNetworkUrl
                .replaceAll("http://", "")
                .replaceAll("https://", "")
                .replaceAll("/.*", "");

        TemplateGenerator generator = new TemplateGenerator(rootDir, name, namespace).withRepositoryHost(repositoryHost);
        Map<Path, String> templateContent = generator.generate();
        Map<Path, String> devTemplateContent = generator.generate(true);

        templateContent.forEach((p, c) -> {
            try {
                Files.createDirectories(p.getParent());
                Files.writeString(p, c);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write file: " + path, e);
            }
        });

        devTemplateContent.forEach((p, c) -> {
            try {
                Files.createDirectories(p.getParent());
                Files.writeString(p, c);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write file: " + path, e);
            }
        });

        Path backstageDir = rootDir.resolve(".backstage");
        Path templatesDir = backstageDir.resolve("templates");
        Path templateDir = templatesDir.resolve(name);
        Path skeletonDir = templateDir.resolve("skeleton");
        Path devTemplateDir = templatesDir.resolve(name + "-dev");
        Path devSkeletonDir = devTemplateDir.resolve("skeleton");

        /**
         * TODO: Currently its not possible to generate addtionafiles (e.g. openapi, helm, argocd resources).
         * So, let's copy them from the template that has been created at build time.
         * This is just a workaround and we need to do it properly.
         **/
        try {
            Files.walk(skeletonDir)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        Path relativePath = skeletonDir.relativize(p);
                        Path devPath = devSkeletonDir.resolve(relativePath);
                        if (!Files.exists(devPath)) {
                            try {
                                Files.createDirectories(devPath.getParent());
                                Files.copy(p, devPath);
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to copy file: " + p, e);
                            }
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy skeleton files to dev skeleton directory", e);
        }

        Path templateYamlPath = templateDir.resolve("template.yaml");
        return templateContent.get(templateYamlPath);
    }

    public boolean install(String path, String name, String remoteUrl, String giteaSharedNetworkUrl, String giteaUsername,
            String giteaPassword, String remoteName,
            String remoteBranch, boolean commit, boolean push) {
        Path rootDir = Paths.get(path);
        Path templatePath = rootDir.resolve(".backstage").resolve("templates").resolve(name).resolve("template.yaml");
        Path devTemplatePath = rootDir.resolve(".backstage").resolve("templates").resolve(name + "-dev")
                .resolve("template.yaml");

        generate(path, name, "default", giteaSharedNetworkUrl);

        Path relativeTemplatePath = rootDir.relativize(templatePath);
        Path relativeDevTemplatePath = rootDir.relativize(devTemplatePath);

        Gitea gitea = Gitea.create(remoteUrl, giteaUsername, giteaPassword, "dev", name, rootDir);
        gitea.pushProject(rootDir);
        gitea.withSharedReference(relativeTemplatePath, targetUrl -> {
            installTemplate(targetUrl);
            LOG.debugf("Backstage Template published at: %s", targetUrl);
        });

        gitea.withSharedReference(relativeDevTemplatePath, targetUrl -> {
            installTemplate(targetUrl);
            LOG.debugf("Backstage Dev Template published at: %s", targetUrl);
        });

        return true;
    }

    public BackstageClient getBackstageClient() {
        return backstageClient;
    }

    private void installTemplate(Optional<String> targetUrl) {
        installTemplate(targetUrl.orElseThrow(() -> new RuntimeException("No target URL found.")));
    }

    private void installTemplate(String targetUrl) {
        Optional<Location> existingLocation = getBackstageClient().entities().list().stream()
                .filter(e -> e.getKind().equals("Location"))
                .map(e -> (Location) e)
                .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                        || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                .findFirst();

        if (existingLocation.isPresent()) {
            Location l = existingLocation.get();
            String entityRef = "location:" + l.getMetadata().getNamespace().orElse("default") + "/" + l.getMetadata().getName();
            getBackstageClient().entities().withKind("location").withName(l.getMetadata().getName()).inNamespace("default")
                    .refresh();
        } else {
            getBackstageClient().locations().createFromUrl(targetUrl);
        }
    }
}
