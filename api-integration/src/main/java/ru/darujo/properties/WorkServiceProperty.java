package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "integration.work-service")
public class WorkServiceProperty extends ServiceProperty {
}
