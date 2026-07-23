package ru.darujo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import ru.darujo.dto.information.SendMessage;
import ru.darujo.dto.information.SendServiceInt;
import ru.darujo.service.DefaultEmailService;
import ru.darujo.type.MessageSenderType;

@Configuration
@Slf4j
@Import(AppConfigIntegration.class)
public class AppConfig {

    @ConditionalOnMissingBean(name = "telegramServiceIntegration")
    @Bean("telegramServiceIntegration")
    public SendServiceInt telegramServiceIntegration() {
        return getDefault(MessageSenderType.Telegram);
    }

    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    @Bean("mailServiceIntegration")
    public SendServiceInt mailServiceIntegration(JavaMailSender emailSender) {
        return new DefaultEmailService(emailSender);

    }

    @ConditionalOnMissingBean(name = "mailServiceIntegration")
    @Bean("mailServiceIntegration")
    public SendServiceInt getDefaultMailServiceIntegration() {
        return getDefault(MessageSenderType.Email);
    }

    private static SendServiceInt defaultService;

    private SendServiceInt getDefault(MessageSenderType messageSenderType) {
        log.warn("Не подключен сервис {} все сообщения игнорируются", messageSenderType.name());
        if (defaultService == null) {
            defaultService = new SendServiceInt() {
                @Override
                public MessageSenderType getMessageSenderType() {
                    return messageSenderType;
                }

                @Override
                public boolean sendMessage(SendMessage sendMessage) throws RuntimeException {
                    return true;
                }
            };
        }
        return defaultService;

    }
}
