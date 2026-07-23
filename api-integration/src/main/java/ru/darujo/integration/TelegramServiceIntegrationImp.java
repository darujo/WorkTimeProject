package ru.darujo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.type.MessageSenderType;

@Slf4j
public class TelegramServiceIntegrationImp extends MessagerServiceIntegrationImp {
    @Override
    public ServiceType getServiceType() {
        return ServiceType.TELEGRAM;
    }

    public TelegramServiceIntegrationImp(WebClient webClientTelegram) {
        super.setWebClient(webClientTelegram);
    }

    @Override
    public MessageSenderType getMessageSenderType() {
        return MessageSenderType.Telegram;
    }
}
