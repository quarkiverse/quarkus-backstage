package io.quarkiverse.backstage.runtime.devui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

import io.quarkiverse.backstage.common.dsl.GitActions;
import io.quarkiverse.backstage.common.template.TemplateGenerator;
import io.quarkiverse.backstage.common.utils.Git;
import io.quarkiverse.backstage.rest.CreateLocationRequest;
import io.quarkiverse.backstage.rest.RefreshEntity;
import io.quarkiverse.backstage.runtime.BackstageClient;
import io.quarkiverse.backstage.v1alpha1.Location;

@ActivateRequestContext
public class BackstageTemplateJsonRPCService {

    @Inject
    BackstageClient backstageClient;

    public String generate(String path, String name, String namespace) {
        Path rootDir = Paths.get(path);
        TemplateGenerator generator = new TemplateGenerator(rootDir, name, namespace);
        Map<Path, String> templateContent = generator.generate();

        templateContent.forEach((p, c) -> {
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

        Path templateYamlPath = templateDir.resolve("template.yaml");
        return templateContent.get(templateYamlPath);
    }

    public boolean install(String path, String name, String remote, String branch, boolean commit, boolean push) {
        Path rootDir = Paths.get(path);
        Path templatePath = rootDir.resolve(".backstage").resolve("templates").resolve(name).resolve("template.yaml");

        Optional<String> url = Git.getUrl(remote, branch, rootDir.relativize(templatePath));
        if (url.isEmpty()) {
            System.out.println("No git remote url found. Template cannot be published. Aborting.");
            return false;
        }

        if (commit && push && commitAndPush(rootDir, remote, branch)) {
            System.out.println("Backstage Template pushed to the remote repository.");
        } else {
            System.out.println("Backstage Template not pushed to the remote repository. Aborting.");
            return false;
        }

        System.out.println("Backstage Template published at: " + url.get());
        final String targetUrl = url.get();

        Optional<Location> existingLocation = getBackstageClient().getAllEntities().stream()
                .filter(e -> e.getKind().equals("Location"))
                .map(e -> (Location) e)
                .filter(e -> targetUrl.equals(e.getSpec().getTarget())
                        || (e.getSpec().getTargets() != null && e.getSpec().getTargets().contains(targetUrl)))
                .findFirst();

        if (existingLocation.isPresent()) {
            Location l = existingLocation.get();
            String entityRef = "location:" + l.getMetadata().getNamespace().orElse("default") + "/" + l.getMetadata().getName();
            getBackstageClient().refreshEntity(new RefreshEntity(entityRef));
        } else {
            CreateLocationRequest request = new CreateLocationRequest("url", targetUrl);
            getBackstageClient().createLocation(request);
        }
        return true;
    }

    private boolean commitAndPush(Path rootDir, String remote, String branch) {
        Path dotBackstage = rootDir.relativize(rootDir.resolve(".backstage"));
        Path catalogInfoYaml = rootDir.relativize(rootDir.resolve("catalog-info.yaml"));
        GitActions.createTempo()
                .checkoutOrCreateBranch(remote, branch)
                .importFiles(rootDir, dotBackstage, catalogInfoYaml)
                .commit("Generated backstage resources.", dotBackstage, catalogInfoYaml);
        return true;
    }

    public BackstageClient getBackstageClient() {
        return backstageClient;
    }

}
