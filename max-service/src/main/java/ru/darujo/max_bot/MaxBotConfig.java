package ru.darujo.max_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.darujo.service.FileSaverService;
import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.client.MaxClientConfig;
import ru.max.botapi.core.PollingErrorHandler;
import ru.max.botapi.spring.longpolling.MaxLongPollingProperties;
import ru.max.botapi.spring.webhook.MaxWebhookProperties;

import java.nio.file.Path;
import java.util.Arrays;

@Slf4j
@Component
public class MaxBotConfig {
    private static final String[] CERTIFICATES = {
            "certs/rootca_ssl_rsa2022.crt",
            "certs/subca_ssl_rsa2024.crt",
            "certs/russian_trusted_root_ca.cer",
            "certs/russian_trusted_root_ca_gost_2025.cer",
            "certs/russian_trusted_sub_ca.cer",
            "certs/russian_trusted_sub_ca_2024.cer",
            "certs/russian_trusted_sub_ca_gost_2025.cer"
    };

    @Bean
    @ConditionalOnProperty(prefix = "max.bot.longpolling", name = "token")
    public MaxBotAPI maxBotAPI(MaxLongPollingProperties properties, FileSaverService fileSaverService) {
        log.info("longpolling https://platform-api2.max.ru");
        return maxBotAPI(properties.getToken(), fileSaverService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "max.bot.webhook", name = "token")
    public MaxBotAPI maxBotAPI(MaxWebhookProperties properties, FileSaverService fileSaverService) {
        log.info("webhook https://platform-api2.max.ru");
        return maxBotAPI(properties.getToken(), fileSaverService);
    }

    public MaxBotAPI maxBotAPI(String token, FileSaverService fileSaverService) {

        Path[] paths = Arrays.stream(CERTIFICATES).map(pathRes -> fileSaverService.resourceToFile(pathRes).toPath()).toArray(Path[]::new);
        MaxClientConfig config = MaxClientConfig.builder()
                .trustedCertificates(paths)
                .build();
        return MaxBotAPI.create(token, config);
    }

    @Bean
    PollingErrorHandler pollingErrorHandler() {
        return e -> log.error("Ошибка опроса", e);
    }
}
