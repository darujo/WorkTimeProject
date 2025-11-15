package ru.darujo.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.darujo.properties.PropertyConnectionInterface;

import java.util.concurrent.TimeUnit;

@Log4j2
public class WebClientConfig {

    public WebClient webClient(PropertyConnectionInterface propertyConnection) {
        log.info(propertyConnection.getUrl());
//        TcpClient tcpClient = TcpClient
//                .create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, propertyConnection.getConnectionTimeOut())
//                .doOnConnected(connection -> {
//                    connection.addHandlerLast(new ReadTimeoutHandler(propertyConnection.getReadTimeOut(), TimeUnit.MILLISECONDS));
//                    connection.addHandlerLast(new WriteTimeoutHandler(propertyConnection.getWriteTimeOut(), TimeUnit.MILLISECONDS));
//                });
        HttpClient httpClient = HttpClient.create().option(
                ChannelOption.CONNECT_TIMEOUT_MILLIS, propertyConnection.getConnectionTimeOut()
        ).doOnConnected(connection -> {
            connection.addHandlerLast(new ReadTimeoutHandler(propertyConnection.getReadTimeOut(), TimeUnit.MILLISECONDS));
            connection.addHandlerLast(new WriteTimeoutHandler(propertyConnection.getWriteTimeOut(), TimeUnit.MILLISECONDS));
        });
//        try {
//            httpClient = httpClient.secure();
//            // или
//            SslContext sslContext = SslContextBuilder
//                    .forClient()
//                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                    .build();
//            httpClient = httpClient.secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
//
//        } catch (SSLException e) {
//            throw new RuntimeException(e);
//        }
        return WebClient
                .builder()
                .baseUrl(propertyConnection.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
