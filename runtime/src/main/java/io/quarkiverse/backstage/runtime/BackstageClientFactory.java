package io.quarkiverse.backstage.runtime;

import java.net.URI;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkus.arc.DefaultBean;

@ApplicationScoped
public class BackstageClientFactory {

    @DefaultBean
    @Produces
    @Singleton
    public BackstageClient produce(BackstageRuntimeConfiguration config) {
        String url = config.url().orElse("http://localhost:7000");
        String host = URI.create(url).getHost();
        int port = URI.create(url).getPort();
        String token = config.token().orElse(null);
        return new BackstageClient(host, port, token);
    }
}
