package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.RateServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        RateServiceProperty.class

)

public class AppConfigRate extends WebClientConfig{
    private RateServiceProperty rateServiceProperty;
    @Autowired
    public void setRateServiceProperty(RateServiceProperty rateServiceProperty){
        this.rateServiceProperty = rateServiceProperty;
    }

    @Bean("webClientRate")
    public WebClient webClientRate(){
        return webClient(rateServiceProperty);
    }

}
