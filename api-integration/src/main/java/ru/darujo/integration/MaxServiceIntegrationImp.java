package ru.darujo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.type.MessageSenderType;

@Slf4j
public class MaxServiceIntegrationImp extends MessagerServiceIntegrationImp {
    @Override
    public ServiceType getServiceType() {
        return ServiceType.MAX;
    }

    public MaxServiceIntegrationImp(WebClient webClientMax) {
        super.setWebClient(webClientMax);
    }

    @Override
    public MessageSenderType getMessageSenderType() {
        return MessageSenderType.Max;
    }
}
