package ru.darujo.integration;

public interface ServiceIntegration<T extends Enum<?>> {
    T getServiceType();
    void test();

    void shutDown(String token);
}
