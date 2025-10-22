package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties( prefix = "integration.information-service")
public class InfoServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
