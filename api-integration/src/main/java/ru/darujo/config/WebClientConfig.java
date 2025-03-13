package ru.darujo.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import ru.darujo.properties.PropertyConnectionInterface;

import java.util.concurrent.TimeUnit;

public class WebClientConfig {

    public WebClient webClient (PropertyConnectionInterface propertyConnection){
        System.out.println(propertyConnection.getUrl());
        TcpClient tcpClient = TcpClient
                .create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS,propertyConnection.getConnectionTimeOut())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(propertyConnection.getReadTimeOut(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(propertyConnection.getWriteTimeOut(),TimeUnit.MILLISECONDS));
                });
        return WebClient
                .builder()
                .baseUrl(propertyConnection.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()))
                .build();
    }
}
