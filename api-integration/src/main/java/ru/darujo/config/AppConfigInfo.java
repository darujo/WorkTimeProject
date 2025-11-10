package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.InfoServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        InfoServiceProperty.class

)

public class AppConfigInfo extends WebClientConfig{
    private InfoServiceProperty infoServiceProperty;
    @Autowired
    public void setInfoServiceProperty(InfoServiceProperty infoServiceProperty){
        this.infoServiceProperty = infoServiceProperty;
    }

    @Bean("webClientInfo")
    public WebClient webClientRate(){
        return webClient(infoServiceProperty);
    }

}
