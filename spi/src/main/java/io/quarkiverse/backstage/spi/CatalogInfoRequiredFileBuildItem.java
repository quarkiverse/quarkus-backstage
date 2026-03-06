package io.quarkiverse.backstage.spi;

import java.nio.file.Path;

import io.quarkus.builder.item.MultiBuildItem;

/**
 * A build item that represents an additional file that should be added to the Backstage location.
 * Files catalog-info.yaml may reference additional files.
 * These files need to be part of the location (usually git repository) so that Backstage can read them.
 **/
public final class CatalogInfoRequiredFileBuildItem extends MultiBuildItem {

    private final Path path;

    public CatalogInfoRequiredFileBuildItem(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
