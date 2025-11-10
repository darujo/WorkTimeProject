package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties( prefix = "integration.telegram-service")
public class TelegramServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
