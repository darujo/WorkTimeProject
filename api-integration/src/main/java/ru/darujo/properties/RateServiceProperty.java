package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties( prefix = "integration.rate-service")
public class RateServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
