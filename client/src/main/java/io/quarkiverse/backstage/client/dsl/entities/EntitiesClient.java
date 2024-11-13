package io.quarkiverse.backstage.client.dsl.entities;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkiverse.backstage.client.BackstageClientContext;
import io.quarkiverse.backstage.client.model.EntityQueryResult;
import io.quarkiverse.backstage.client.model.RefreshEntity;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.v1alpha1.Entity;

public class EntitiesClient implements EntitiesInterface,
        WithNameInterface<InNamespaceInterface<GetRefreshInterface<Entity, Boolean>>>,
        InNamespaceInterface<GetRefreshInterface<Entity, Boolean>>,
        GetRefreshInterface<Entity, Boolean>,
        DeleteInterface<Boolean> {

    private BackstageClientContext context;
    private String uid;
    private String kind;
    private String name;
    private String namespace;

    public EntitiesClient(BackstageClientContext context) {
        this.context = context;
    }

    public EntitiesClient(BackstageClientContext context, String uid, String kind, String name, String namespace) {
        this.context = context;
        this.uid = uid;
        this.kind = kind;
        this.name = name;
        this.namespace = namespace;
    }

    @Override
    public WithNameInterface<InNamespaceInterface<GetRefreshInterface<Entity, Boolean>>> withKind(String kind) {
        return new EntitiesClient(context, uid, kind, name, namespace);
    }

    @Override
    public InNamespaceInterface<GetRefreshInterface<Entity, Boolean>> withName(String name) {
        return new EntitiesClient(context, uid, kind, name, namespace);
    }

    @Override
    public DeleteInterface<Boolean> withUID(String uid) {
        return new EntitiesClient(context, uid, kind, name, namespace);
    }

    @Override
    public GetRefreshInterface<Entity, Boolean> inNamespace(String namespace) {
        return new EntitiesClient(context, uid, kind, name, namespace);
    }

    @Override
    public List<Entity> create(Collection<Entity> entities) {
        try {
            return context.getWebClient().post("/api/catalog/entities")
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .sendJson(entities)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to create entities: " + response.statusMessage());
                        }
                    })
                    .thenApply(s -> Serialization.unmarshalAsList(s)).get().getItems();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Entity> list() {
        try {
            return context.getWebClient().get("/api/catalog/entities")
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .send()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to get entities: " + response.statusMessage());
                        }
                    }).thenApply(s -> Serialization.unmarshal(s, new TypeReference<List<Entity>>() {
                    }))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Entity> list(String filter) {
        try {
            return context.getWebClient().get("/api/catalog/entities/by-query")
                    .addQueryParam("filter", filter)
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .send()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to get entities: " + response.statusMessage());
                        }
                    })
                    .thenApply(s -> Serialization.unmarshal(s, EntityQueryResult.class)).get().getItems();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Boolean delete() {
        return context.getWebClient().delete("/api/catalog/entities/by-uid/" + uid)
                .putHeader("Authorization", "Bearer " + context.getToken())
                .putHeader("Content-Type", "application/json")
                .send()
                .onFailure(throwable -> {
                    throw new RuntimeException("Failed to delete entity: " + throwable.getMessage());
                }).succeeded();
    }

    @Override
    public Entity get() {
        try {
            String path = uid != null ? "/api/catalog/entities/by-uid/" + uid
                    : "/api/catalog/entities/by-name/" + kind + "/" + namespace + "/" + name;
            return context.getWebClient().get(path)
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .send()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to get entity: " + response.statusMessage());
                        }
                    })
                    .thenApply(s -> Serialization.unmarshal(s, Entity.class)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Boolean refresh() {
        RefreshEntity refresh = new RefreshEntity();
        refresh.setEntityRef(kind + ":" + namespace + "/" + name);
        return context.getWebClient().post("/api/catalog/refresh")
                .putHeader("Authorization", "Bearer " + context.getToken())
                .putHeader("Content-Type", "application/json")
                .sendJson(refresh)
                .onFailure(throwable -> {
                    throw new RuntimeException("Failed to refresh entity: " + throwable.getMessage());
                }).succeeded();
    }
}
