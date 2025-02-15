package ru.darujo.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import ru.darujo.properties.WorkServiceProperty;
import ru.darujo.properties.PropertyConnectionInterface;

import java.util.concurrent.TimeUnit;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        WorkServiceProperty.class
)
public class AppConfig {
    private WorkServiceProperty workServiceProperty;
    @Autowired
    public void setWorkServiceProperty(WorkServiceProperty workServiceProperty){
        this.workServiceProperty = workServiceProperty;
    }

    private WebClient webClient (PropertyConnectionInterface propertyConnection){
        TcpClient tcpClient = TcpClient
                .create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS,propertyConnection.getConnectionTimeOut())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(propertyConnection.getReadTimeOut(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(propertyConnection.getWriteTimeOut(),TimeUnit.MILLISECONDS));
                });
        return WebClient
                .builder()
                .baseUrl(propertyConnection.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient))) // TODO Убрать устаревший метод
                .build();
    }
    @Bean
    public WebClient webClientBasket(){
        return webClient(workServiceProperty);
    }
}
