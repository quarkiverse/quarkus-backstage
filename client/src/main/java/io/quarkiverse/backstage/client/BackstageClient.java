package io.quarkiverse.backstage.client;

import io.quarkiverse.backstage.client.dsl.BackstageDSL;
import io.quarkiverse.backstage.client.dsl.entities.EntitiesClient;
import io.quarkiverse.backstage.client.dsl.entities.EntitiesInterface;
import io.quarkiverse.backstage.client.dsl.locations.LocationsClient;
import io.quarkiverse.backstage.client.dsl.locations.LocationsInterface;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class BackstageClient implements BackstageDSL {

    private String host;
    private int port;
    private String token;
    private WebClient client;
    private BackstageClientContext context;

    public BackstageClient(String url, String token) {
        this(getHost(url), getPort(url), token);
    }

    public BackstageClient(String host, int port, String token) {
        this.host = host;
        this.port = port;
        this.token = token;
        this.context = new BackstageClientContext(host, port, token);
        WebClientOptions options = new WebClientOptions().setDefaultHost(host).setDefaultPort(port);
        this.client = WebClient.create(Vertx.vertx(), options);
    }

    private static String getHost(String url) {
        return url.split(":")[0];
    }

    private static int getPort(String url) {
        return Integer.parseInt(url.split(":")[1]);
    }

    @Override
    public EntitiesInterface entities() {
        return new EntitiesClient(context);
    }

    @Override
    public LocationsInterface locations() {
        return new LocationsClient(context);
    }
}
