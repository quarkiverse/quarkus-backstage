package io.quarkus.backstage.model.builder;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface Builder<T> {

    T build();

}