package io.quarkiverse.backstage.runtime;

import java.net.URI;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;

public class BackstageClientFactory {

    @Inject
    BackstageConfiguration config;

    @Inject
    BackstageClientHeaderFactory headerFactory;

    @Produces
    public BackstageClient produce() {
        return QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(config.url().orElse("http://localhost:7007")))
                .clientHeadersFactory(headerFactory)
                .build(BackstageClient.class);
    }
}
