package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "integration.information-service")
public class InfoServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
