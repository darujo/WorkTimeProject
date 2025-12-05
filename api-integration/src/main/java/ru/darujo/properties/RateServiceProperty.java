package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "integration.rate-service")
public class RateServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
