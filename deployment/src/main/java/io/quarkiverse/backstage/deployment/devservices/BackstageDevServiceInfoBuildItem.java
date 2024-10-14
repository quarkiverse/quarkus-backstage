package io.quarkiverse.backstage.deployment.devservices;

import io.quarkus.builder.item.SimpleBuildItem;

public final class BackstageDevServiceInfoBuildItem extends SimpleBuildItem {

    private final String url;
    private final String token;

    public BackstageDevServiceInfoBuildItem(String url, String token) {
        this.url = url;
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

}
