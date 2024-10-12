package com.goodquestion.edutrek_server.utility_service;

@FunctionalInterface
public interface ThreeFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
