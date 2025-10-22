package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.TelegramServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        TelegramServiceProperty.class

)

public class AppConfigTelegram extends WebClientConfig{
    private TelegramServiceProperty rateServiceProperty;
    @Autowired
    public void setRateServiceProperty(TelegramServiceProperty rateServiceProperty){
        this.rateServiceProperty = rateServiceProperty;
    }

    @Bean("webClientTelegram")
    public WebClient webClientRate(){
        return webClient(rateServiceProperty);
    }

}
