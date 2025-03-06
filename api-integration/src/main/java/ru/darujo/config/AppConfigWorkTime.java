package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.WorkTimeServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        WorkTimeServiceProperty.class

)

public class AppConfigWorkTime extends WebClientConfig{
    private WorkTimeServiceProperty workTimeServiceProperty;
    @Autowired
    public void setWorkTimeServiceProperty(WorkTimeServiceProperty workTimeServiceProperty){
        this.workTimeServiceProperty = workTimeServiceProperty;
    }

    @Bean("webClientWorkTime")
    public WebClient webClientWorkTime(){
        return webClient(workTimeServiceProperty);
    }

}
