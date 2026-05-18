package ru.darujo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import ru.darujo.integration.AdminInfoService;

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

}
