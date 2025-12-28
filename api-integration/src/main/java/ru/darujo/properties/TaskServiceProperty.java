package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "integration.task-service")
public class TaskServiceProperty extends ServiceProperty {
}
