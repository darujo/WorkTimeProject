package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.CalendarServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        CalendarServiceProperty.class
)
public class AppConfigCalendar extends WebClientConfig {
    private CalendarServiceProperty calendarServiceProperty;
    @Autowired
    public void setCalendarServiceProperty(CalendarServiceProperty calendarServiceProperty){
        this.calendarServiceProperty = calendarServiceProperty;
    }

    @Bean
    public WebClient webClientCalendar(){
        return webClient(calendarServiceProperty);
    }
}
