package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.WorkServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        WorkServiceProperty.class

)

public class AppConfigWork extends WebClientConfig{
    private WorkServiceProperty workServiceProperty;
    @Autowired
    public void setWorkServiceProperty(WorkServiceProperty workServiceProperty){
        this.workServiceProperty = workServiceProperty;
    }

    @Bean("webClientWork")
    public WebClient webClientWork(){
        return webClient(workServiceProperty);
    }

}
