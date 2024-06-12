package io.quarkiverse.backstage.client;

import java.util.Set;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkiverse.backstage.v1alpha1.Entity;

@RegisterRestClient(configKey = "quarkus.backstage")
public interface BackstageClient {

    @GET
    @Path("/entities")
    Set<Entity> getAllEntities();

    @GET
    @Path("/entities/by-uid/{uid}")
    Entity getEntity(String uid);

    @GET
    @Path("/entities/by-name/{kind}/{namespace}/{name}")
    Entity getEntities(String kind, String namespace, String name);

    @DELETE
    @Path("/entities/by-uid/{uid}")
    void deleteEntity(String uid);

    @POST
    @Path("/entities")
    Set<Entity> createEntities(Set<Entity> entities);

}
