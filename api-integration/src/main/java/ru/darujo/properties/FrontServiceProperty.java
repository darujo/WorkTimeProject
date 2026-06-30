package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.front-service")
public class FrontServiceProperty extends ServiceProperty {
}
