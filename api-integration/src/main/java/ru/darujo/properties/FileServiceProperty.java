package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.file-service")
public class FileServiceProperty extends ServiceProperty {
}
