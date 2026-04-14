package ru.darujo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.integration.TelegramServiceIntegration;
import ru.darujo.properties.TelegramServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")

@EnableConfigurationProperties(
        TelegramServiceProperty.class

)

public class AppConfigTelegram extends WebClientConfig{


    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.telegram", name = "enable")
    public WebClient webClientTelegram(TelegramServiceProperty telegramServiceProperty) {
        return webClient(telegramServiceProperty);
    }

    @Bean
    @ConditionalOnBean(name = "webClientTelegram")
    public TelegramServiceIntegration telegramServiceIntegration(@Qualifier("webClientTelegram") WebClient webClientTelegram) {
        return new TelegramServiceIntegration(webClientTelegram);
    }

}
