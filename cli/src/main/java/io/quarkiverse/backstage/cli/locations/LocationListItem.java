package io.quarkiverse.backstage.cli.locations;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.quarkiverse.backstage.v1alpha1.Location;

public class LocationListItem {

    private String apiVersion;
    private String kind;
    private String name;
    private String id;
    private String targets;

    public LocationListItem(String apiVersion, String kind, String name, String id, String targets) {
        this.apiVersion = apiVersion;
        this.kind = kind;
        this.name = name;
        this.id = id;
        this.targets = targets;
    }

    public static LocationListItem from(Location location) {
        return new LocationListItem(location.getApiVersion(), location.getKind(), location.getMetadata().getName(),
                location.getMetadata().getUid() != null ? location.getMetadata().getUid().orElse("") : "",
                getTargets(location));
    }

    private static String getTargets(Location location) {
        return Stream.concat(
                Stream.of(location.getSpec().getTarget()),
                location.getSpec().getTargets() != null ? location.getSpec().getTargets().stream() : Stream.empty())
                .collect(Collectors.joining(", "));
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargets() {
        return targets;
    }

    public String[] getFields() {
        return new String[] { id, name, targets };
    }
}
