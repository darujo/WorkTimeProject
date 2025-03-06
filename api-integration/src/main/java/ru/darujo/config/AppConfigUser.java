package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.UserServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        UserServiceProperty.class
)
public class AppConfigUser extends WebClientConfig {
    private UserServiceProperty userServiceProperty;
    @Autowired
    public void setUserServiceProperty(UserServiceProperty userServiceProperty){
        this.userServiceProperty = userServiceProperty;
    }

    @Bean
    public WebClient webClientUser(){
        return webClient(userServiceProperty);
    }
}
