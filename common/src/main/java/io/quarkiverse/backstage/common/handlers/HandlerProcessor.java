package io.quarkiverse.backstage.common.handlers;

import java.nio.file.Path;
import java.util.function.BiConsumer;

/**
 * Callback passed as context to the custom build handlers.
 * Handlers only see it as a {@link BiConsumer} and only pass JDK types to it, so that it also works when the
 * deployment is isolated and the handler is loaded by a different class loader than the caller.
 */
public interface HandlerProcessor<T> extends BiConsumer<T, Path[]> {

    void process(T obj, Path... additionalPaths);

    @Override
    default void accept(T obj, Path[] additionalPaths) {
        process(obj, additionalPaths);
    }
}
