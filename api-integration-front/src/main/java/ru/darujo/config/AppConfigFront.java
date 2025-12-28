package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.FrontServiceProperty;

@Configuration
@PropertySource("classpath:integration-front.properties")
@EnableConfigurationProperties(
        FrontServiceProperty.class
)
public class AppConfigFront extends WebClientConfig {
    private FrontServiceProperty frontServiceProperty;

    @Autowired
    public void setFrontServiceProperty(FrontServiceProperty frontServiceProperty) {
        this.frontServiceProperty = frontServiceProperty;
    }

    @Bean
    public WebClient webClientFront() {
        return webClient(frontServiceProperty);
    }
}
