package io.quarkiverse.backstage.client.dsl.templates;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.quarkiverse.backstage.client.BackstageClientContext;
import io.quarkiverse.backstage.client.model.InstantiateErrorResponse;
import io.quarkiverse.backstage.client.model.InstantiateRequest;
import io.quarkiverse.backstage.client.model.InstantiateResponse;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;

public class TemplatesClient implements TemplatesInterface, InNamespaceGetInstantiateInterface<Template, String>,
        GetInstantiateInterface<Template, String> {

    private BackstageClientContext context;
    private String name;
    private String namespace = "default";

    public TemplatesClient(BackstageClientContext context) {
        this.context = context;
    }

    public TemplatesClient(BackstageClientContext context, String name, String namespace) {
        this.context = context;
        this.name = name;
        this.namespace = namespace;
    }

    @Override
    public InNamespaceGetInstantiateInterface<Template, String> withName(String name) {
        return new TemplatesClient(context, name, namespace);
    }

    @Override
    public GetInstantiateInterface<Template, String> inNamespace(String namespace) {
        return new TemplatesClient(context, name, namespace);
    }

    @Override
    public Template get() {
        try {
            return context.getWebClient().get("/api/catalog/entities/by-name/template/" + namespace + "/" + name)
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .send()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to get template: " + response.statusMessage());
                        }
                    })
                    .thenApply(s -> Serialization.unmarshal(s, Template.class)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String instantiate(Map<String, Object> values) {
        InstantiateRequest request = new InstantiateRequest();
        request.setTemplateRef("template:" + namespace + "/" + name);
        try {
            InstantiateResponse resp = context.getWebClient().post("/api/scaffolder/v2/tasks")
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .sendJson(request)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200 || response.statusCode() == 201) {
                            return response.bodyAsString();
                        } else {
                            InstantiateErrorResponse errorResponse = Serialization.unmarshal(response.bodyAsString(),
                                    InstantiateErrorResponse.class);
                            throw new RuntimeException("Failed instantiate template: " + getErrorDetails(errorResponse));
                        }
                    }).thenApply(response -> {
                        return Serialization.unmarshal(response, InstantiateResponse.class);
                    }).get();
            return resp.getId();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String getErrorDetails(InstantiateErrorResponse errorResponse) {
        StringBuilder sb = new StringBuilder();
        errorResponse.getErrors().forEach(e -> {
            sb.append(e.getMessage()).append("\n");
        });
        return sb.toString();
    }
}
