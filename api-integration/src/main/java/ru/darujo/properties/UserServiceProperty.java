package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "integration.user-service")
public class UserServiceProperty extends ServiceProperty {
}
