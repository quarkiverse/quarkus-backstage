package io.quarkiverse.backstage.client.dsl.locations;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkiverse.backstage.client.BackstageClientContext;
import io.quarkiverse.backstage.client.model.AnalyzeLocationRequest;
import io.quarkiverse.backstage.client.model.AnalyzeLocationResponse;
import io.quarkiverse.backstage.client.model.CreateLocationRequest;
import io.quarkiverse.backstage.client.model.CreateLocationResponse;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.client.model.LocationItem;
import io.quarkiverse.backstage.client.model.RefreshEntity;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.v1alpha1.Location;

public class LocationsClient implements LocationsInterface,
        WithNameInterface<InNamespaceInterface<GetByEntityRefreshInterface<LocationEntry, Boolean>>>,
        InNamespaceInterface<GetByEntityRefreshInterface<LocationEntry, Boolean>>,
        GetByIdDeleteInterface<LocationEntry, Boolean>, GetByEntityRefreshInterface<LocationEntry, Boolean> {

    private BackstageClientContext context;
    private String id;
    private String kind;
    private String name;
    private String namespace;

    public LocationsClient(BackstageClientContext context) {
        this.context = context;
    }

    public LocationsClient(BackstageClientContext context, String id, String kind, String name, String namespace) {
        this.context = context;
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.namespace = namespace;
    }

    @Override
    public GetByIdDeleteInterface<LocationEntry, Boolean> withId(String id) {
        return new LocationsClient(context, id, kind, name, namespace);
    }

    @Override
    public WithNameInterface<InNamespaceInterface<GetByEntityRefreshInterface<LocationEntry, Boolean>>> withKind(String kind) {
        return new LocationsClient(context, id, kind, name, namespace);
    }

    @Override
    public InNamespaceInterface<GetByEntityRefreshInterface<LocationEntry, Boolean>> withName(String name) {
        return new LocationsClient(context, id, kind, name, namespace);
    }

    @Override
    public GetByEntityRefreshInterface<LocationEntry, Boolean> inNamespace(String namespace) {
        return new LocationsClient(context, id, kind, name, namespace);
    }

    @Override
    public CreateLocationResponse createFromUrl(String url) {
        CreateLocationRequest request = new CreateLocationRequest();
        request.setType("url");
        request.setTarget(url);
        return create(request);
    }

    @Override
    public CreateLocationResponse createFromFile(String file) {
        CreateLocationRequest request = new CreateLocationRequest();
        request.setType("file");
        request.setTarget(file);
        return create(request);
    }

    @Override
    public CreateLocationResponse create(Location location) {
        CreateLocationRequest request = new CreateLocationRequest();
        request.setType("url");
        request.setTarget(location.getSpec().getTarget());
        return create(request);
    }

    public CreateLocationResponse create(CreateLocationRequest request) {
        try {
            return context.getWebClient().post("/api/catalog/locations")
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .sendJson(request)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200 || response.statusCode() == 201) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to create location: " + response.statusMessage());
                        }
                    })
                    .thenApply(response -> {
                        return Serialization.unmarshal(response, CreateLocationResponse.class);
                    }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LocationEntry> list() {
        try {
            List<LocationItem> items = context.getWebClient().get("/api/catalog/locations/")
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .send()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to get locations: " + response.statusMessage());
                        }
                    })
                    .thenApply(s -> Serialization.unmarshal(s, new TypeReference<List<LocationItem>>() {
                    }))
                    .get();
            return items.stream().map(LocationItem::getData).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AnalyzeLocationResponse analyze(AnalyzeLocationRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'analyze'");
    }

    @Override
    public Boolean delete() {
        try {
            return context.getWebClient().delete("/api/catalog/locations/" + id)
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .send()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200 || response.statusCode() == 204) {
                            return true;
                        } else {
                            throw new RuntimeException("Failed to delete location: " + id + ":" + response.statusMessage());
                        }
                    }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LocationEntry get() {
        try {
            return context.getWebClient().get("/api/catalog/locations/by-entity/" + kind + "/" + namespace + "/" + name)
                    .putHeader("Authorization", "Bearer " + context.getToken())
                    .putHeader("Content-Type", "application/json")
                    .send()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.bodyAsString();
                        } else {
                            throw new RuntimeException("Failed to get location by entity: " + response.statusMessage());
                        }
                    })
                    .thenApply(s -> Serialization.unmarshal(s, LocationEntry.class)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean refresh() {
        RefreshEntity refresh = new RefreshEntity();
        refresh.setEntityRef("Location:" + namespace + "/" + name);
        return context.getWebClient().post("/api/catalog/refresh")
                .putHeader("Authorization", "Bearer " + context.getToken())
                .putHeader("Content-Type", "application/json")
                .sendJson(refresh)
                .onFailure(throwable -> {
                    throw new RuntimeException("Failed to refresh entity: " + throwable.getMessage());
                }).succeeded();
    }
}
