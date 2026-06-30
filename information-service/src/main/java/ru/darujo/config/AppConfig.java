package ru.darujo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import ru.darujo.dto.information.SendServiceInt;
import ru.darujo.service.DefaultEmailService;

@Configuration
@Slf4j
@Import(AppConfigIntegration.class)
public class AppConfig {

    @ConditionalOnMissingBean(name = "telegramServiceIntegration")
    @Bean("telegramServiceIntegration")
    public SendServiceInt telegramServiceIntegration() {
        return getDefault("telegram");
    }

    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    @Bean("mailServiceIntegration")
    public SendServiceInt mailServiceIntegration(JavaMailSender emailSender) {
        return new DefaultEmailService(emailSender);

    }

    @ConditionalOnMissingBean(name = "mailServiceIntegration")
    @Bean("mailServiceIntegration")
    public SendServiceInt getDefaultMailServiceIntegration() {
        return getDefault("email");
    }

    private static SendServiceInt defaultService;

    private SendServiceInt getDefault(String name) {
        log.warn("Не подключен сервис {} все сообщения игнорируются", name);
        if (defaultService == null) {
            defaultService = sendMessage -> true;
        }
        return defaultService;

    }
}
