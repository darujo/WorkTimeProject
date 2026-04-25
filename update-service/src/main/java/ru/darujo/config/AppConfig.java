package ru.darujo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import ru.darujo.integration.AdminInfoService;
import ru.darujo.integration.ServiceIntegration;

@Configuration
@Component
@Slf4j
@Import(AppConfigIntegration.class)
public class AppConfig {

    @ConditionalOnMissingBean(AdminInfoService.class)
    @Bean("adminInfoService")
    public AdminInfoService adminInfoService() {
        log.warn("Не подключен сервис {} все сообщения игнорируются", "admin");
        return message -> {
        };
    }

    @ConditionalOnMissingBean(name = "telegramServiceIntegration")
    @Bean("telegramServiceIntegration")
    public ServiceIntegration telegramServiceIntegration() {
        log.warn("Не подключен сервис {} все сообщения игнорируются", "telegram");
        return new ServiceIntegration() {
            @Override
            public void test() {

            }

            @Override
            public void shutDown(String token) {

            }

        };
    }
}
