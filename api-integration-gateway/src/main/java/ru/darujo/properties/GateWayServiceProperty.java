package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration.gate-way-service")
public class GateWayServiceProperty extends ServiceProperty {
}
