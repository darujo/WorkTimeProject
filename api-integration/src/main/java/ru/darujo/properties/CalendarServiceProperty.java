package ru.darujo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "integration.calendar-service")
public class CalendarServiceProperty extends ServiceProperty implements PropertyConnectionInterface {
}
