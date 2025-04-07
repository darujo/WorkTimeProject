package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties( prefix = "integration.work-service")
public class WorkServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
