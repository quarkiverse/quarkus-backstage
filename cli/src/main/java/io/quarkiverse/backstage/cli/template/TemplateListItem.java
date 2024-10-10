package io.quarkiverse.backstage.cli.template;

import io.quarkiverse.backstage.v1alpha1.Entity;

public class TemplateListItem {

    private String apiVersion;
    private String kind;
    private String name;
    private String id;

    public TemplateListItem(String apiVersion, String kind, String name, String id) {
        this.apiVersion = apiVersion;
        this.kind = kind;
        this.name = name;
        this.id = id;
    }

    public static TemplateListItem from(Entity entity) {
        return new TemplateListItem(entity.getApiVersion(), entity.getKind(), entity.getMetadata().getName(),
                entity.getMetadata().getUid().orElse(""));
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

    public String[] getFields() {
        return new String[] { apiVersion, kind, name, id };
    }
}
