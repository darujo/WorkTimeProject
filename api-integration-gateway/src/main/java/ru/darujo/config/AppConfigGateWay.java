package ru.darujo.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.GateWayServiceProperty;

import javax.net.ssl.SSLException;

@Configuration
@PropertySource("classpath:integration-gateway.properties")
@EnableConfigurationProperties(
        GateWayServiceProperty.class
)
@Slf4j
public class AppConfigGateWay extends WebClientConfig {
    private GateWayServiceProperty gateWayServiceProperty;

    @Autowired
    public void setGateWayServiceProperty(GateWayServiceProperty gateWayServiceProperty) {
        this.gateWayServiceProperty = gateWayServiceProperty;
    }

    @Bean
    public WebClient webClientGateWay(SslContext sslContext) {
        return webClient(gateWayServiceProperty, sslContext);
    }

    @Bean
    public SslContext sslContext() throws SSLException {
        log.info("Building a customised SslContext");
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                // modify as you wish, e. g. trust a self-signed cert
                .build();
    }
}
