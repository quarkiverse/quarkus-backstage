package io.quarkiverse.backstage.model;

public interface Entity {

    String BACKSTAGE_IO_V1BETA1 = "backstage.io/v1beta1";

    default String getKind() {
        throw new UnsupportedOperationException();
    }

    default String getApiVersion() {
        return BACKSTAGE_IO_V1BETA1;
    }

    default EntityMeta getMetadata() {
        throw new UnsupportedOperationException();
    }

    default Status getStatus() {
        throw new UnsupportedOperationException();
    }
}
