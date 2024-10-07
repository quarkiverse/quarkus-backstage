package io.quarkiverse.backstage.runtime;

import java.util.Collection;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.quarkiverse.backstage.rest.CreateLocationRequest;
import io.quarkiverse.backstage.rest.Location;
import io.quarkiverse.backstage.rest.LocationItem;
import io.quarkiverse.backstage.rest.RefreshEntity;
import io.quarkiverse.backstage.v1alpha1.Entity;

@Path("/api/catalog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BackstageClient {

    @GET
    @Path("/entities")
    List<Entity> getAllEntities();

    @GET
    @Path("/entities/by-uid/{uid}")
    Entity getEntity(String uid);

    @GET
    @Path("/entities/by-name/{kind}/{namespace}/{name}")
    Entity getEntity(String kind, String namespace, String name);

    @DELETE
    @Path("/entities/by-uid/{uid}")
    void deleteEntity(String uid);

    @POST
    @Path("/entities")
    List<Entity> createEntities(Collection<Entity> entities);

    @POST
    @Path("/refresh")
    void refreshEntity(RefreshEntity refresh);

    @GET
    @Path("/locations/")
    List<LocationItem> getLocations();

    @GET
    @Path("/locations/by-entity/{kind}/{namespace}/{name}")
    Location getLocationByEntity(String kind, String namespace, String name);

    @POST
    @Path("/locations")
    String createLocation(CreateLocationRequest request);

    @DELETE
    @Path("/locations/{id}")
    String deleteLocation(String id);

}
