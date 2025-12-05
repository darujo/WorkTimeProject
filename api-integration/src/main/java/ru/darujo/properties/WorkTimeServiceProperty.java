package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties( prefix = "integration.work-time-service")
public class WorkTimeServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
