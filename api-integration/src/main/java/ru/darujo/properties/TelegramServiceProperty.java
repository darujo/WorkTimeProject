package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties( prefix = "integration.telegram-service")
public class TelegramServiceProperty extends ServiceProperty {
}
