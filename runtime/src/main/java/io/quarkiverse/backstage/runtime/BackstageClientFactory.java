package io.quarkiverse.backstage.runtime;

import java.net.URI;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import io.quarkus.arc.DefaultBean;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;

@ApplicationScoped
public class BackstageClientFactory {

    @DefaultBean
    @Produces
    @Singleton
    public BackstageClient produce(BackstageClientHeaderFactory headerFactory, BackstageRuntimeConfiguration config) {
        return QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(config.url().orElse("http://localhost:7007")))
                .clientHeadersFactory(headerFactory)
                .build(BackstageClient.class);
    }
}
