package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.max-service")
public class MaxServiceProperty extends ServiceProperty {
}
