package io.quarkiverse.backstage.common.handlers;

import java.nio.file.Path;

public interface HandlerProcessor<T> {

    void process(T obj, Path... additionalPaths);
}
