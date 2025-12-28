package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@ConditionalOnMissingClass
public class GateWayServiceIntegration extends ServiceIntegration {
    @Autowired
    public void setWebClient(WebClient webClientGateWay) {
        super.setWebClient(webClientGateWay);
    }

}
