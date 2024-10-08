package io.quarkiverse.backstage.runtime;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@Singleton
public class BackstageClientHeaderFactory implements ClientHeadersFactory {

    @Inject
    BackstageConfiguration config;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> clientOutgoingHeaders) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>(clientOutgoingHeaders);
        config.token().ifPresent(token -> headers.add("Authorization", "Bearer " + token));
        return headers;
    }
}
