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
import io.quarkiverse.backstage.common.dsl.GitActions;
import io.quarkiverse.backstage.common.template.TemplateGenerator;
import io.quarkiverse.backstage.common.utils.Git;
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

        Optional<String> templateUrl = Optional.ofNullable(giteaSharedNetworkUrl)
                .map(u -> Git.getUrlFromBase(u, remoteBranch, relativeTemplatePath))
                .orElseGet(() -> Git.getUrl(remoteName, remoteBranch, relativeTemplatePath));

        Optional<String> devTemplateUrl = Optional.ofNullable(giteaSharedNetworkUrl)
                .map(u -> Git.getUrlFromBase(u, remoteBranch, relativeDevTemplatePath))
                .orElseGet(() -> Git.getUrl(remoteName, remoteBranch, relativeDevTemplatePath));

        if (templateUrl.isEmpty()) {
            LOG.warn("No git remote url found. Template cannot be published. Aborting.");
            return false;
        }

        if (commit && push && commitAndPush(rootDir, remoteUrl, remoteName, remoteBranch, giteaUsername, giteaPassword)) {
            LOG.debug("Backstage Template pushed to the remote repository.");
        } else {
            LOG.warn("Backstage Template not pushed to the remote repository. Aborting.");
            return false;
        }

        LOG.debugf("Backstage Template published at: %s", templateUrl.get());
        LOG.debugf("Backstage Dev Template published at: %s", devTemplateUrl.get());

        installTemplate(templateUrl);
        installTemplate(devTemplateUrl);

        return true;
    }

    public BackstageClient getBackstageClient() {
        return backstageClient;
    }

    private boolean commitAndPush(Path rootDir, String remoteUrl, String remoteName, String remoteBranch, String username,
            String password) {
        Path dotBackstage = rootDir.relativize(rootDir.resolve(".backstage"));
        Path catalogInfoYaml = rootDir.relativize(rootDir.resolve("catalog-info.yaml"));
        if (remoteUrl != null) {
            GitActions.createTempo()
                    .addRemote(remoteName, remoteUrl)
                    .createBranch(remoteBranch)
                    .importFiles(rootDir, dotBackstage, catalogInfoYaml)
                    .commit("Generated backstage resources.", dotBackstage, catalogInfoYaml)
                    .push(remoteName, remoteBranch, username, password);
            return true;
        }

        GitActions.createTempo()
                .checkoutOrCreateBranch(remoteName, remoteBranch)
                .importFiles(rootDir, dotBackstage, catalogInfoYaml)
                .commit("Generated backstage resources.", dotBackstage, catalogInfoYaml)
                .push(remoteName, remoteBranch);

        return true;
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
