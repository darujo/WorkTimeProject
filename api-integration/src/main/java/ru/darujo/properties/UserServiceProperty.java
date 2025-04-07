package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties( prefix = "integration.user-service")
public class UserServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
