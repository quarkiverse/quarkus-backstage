package io.quarkiverse.backstage.client;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class BackstageClientContext {

    private final String host;
    private final int port;
    private final String token;
    private final WebClient webClient;

    public BackstageClientContext(String host, int port, String token) {
        this.host = host;
        this.port = port;
        this.token = token;

        WebClientOptions options = new WebClientOptions().setDefaultHost(host).setDefaultPort(port);
        this.webClient = WebClient.create(Vertx.vertx(), options);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getToken() {
        return token;
    }

    public WebClient getWebClient() {
        return webClient;
    }

}
