package ru.darujo.integration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@ConditionalOnMissingClass
public class FrontServiceIntegration extends ServiceIntegration {
    public FrontServiceIntegration(WebClient webClientFront) {
        super.setWebClient(webClientFront);
    }
}
