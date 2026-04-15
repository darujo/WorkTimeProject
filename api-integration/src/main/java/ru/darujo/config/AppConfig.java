package ru.darujo.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.darujo.integration.*;
import ru.darujo.properties.*;

import javax.net.ssl.SSLException;

@Configuration
@PropertySource("classpath:integration.properties")

@EnableConfigurationProperties(
        {
                TelegramServiceProperty.class,
                CalendarServiceProperty.class,
                InfoServiceProperty.class,
                RateServiceProperty.class,
                TaskServiceProperty.class,
                UserServiceProperty.class,
                WorkServiceProperty.class,
                WorkTimeServiceProperty.class,
                FrontServiceProperty.class,
                GateWayServiceProperty.class
        }

)

public class AppConfig extends WebClientConfig {
    // Telegram
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.telegram-service", name = "enable")
    public TelegramServiceIntegration telegramServiceIntegration(TelegramServiceProperty telegramServiceProperty) {
        return new TelegramServiceIntegration(webClient(telegramServiceProperty));
    }

    // Calendar
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.calendar-service", name = "enable")
    public CalendarServiceIntegration calendarServiceIntegration(CalendarServiceProperty calendarServiceProperty) {
        return new CalendarServiceIntegration(webClient(calendarServiceProperty));
    }

    //Information
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.information-service", name = "enable")
    public InfoServiceIntegration infoServiceIntegration(InfoServiceProperty infoServiceProperty) {
        return new InfoServiceIntegration(webClient(infoServiceProperty));
    }

    //Rate
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.rate-service", name = "enable")
    public RateServiceIntegration rateServiceIntegration(RateServiceProperty rateServiceProperty) {
        return new RateServiceIntegration(webClient(rateServiceProperty));
    }

    //Task
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.task-service", name = "enable")
    public TaskServiceIntegration taskServiceIntegration(TaskServiceProperty taskServiceProperty) {
        return new TaskServiceIntegration(webClient(taskServiceProperty));
    }

    //User
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.user-service", name = "enable")
    public UserServiceIntegration userServiceIntegration(UserServiceProperty userServiceProperty) {
        return new UserServiceIntegration(webClient(userServiceProperty));
    }

    //Work
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.work-service", name = "enable")
    public WorkServiceIntegration workServiceIntegration(WorkServiceProperty workServiceProperty) {
        return new WorkServiceIntegration(webClient(workServiceProperty));
    }

    //WorkTime
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.work-time-service", name = "enable")
    public WorkTimeServiceIntegration workTimeServiceIntegration(WorkTimeServiceProperty workTimeServiceProperty) {
        return new WorkTimeServiceIntegration(webClient(workTimeServiceProperty));
    }

    //Front
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.front-service", name = "enable")
    public FrontServiceIntegration frontServiceIntegration(FrontServiceProperty frontServiceProperty) {
        return new FrontServiceIntegration(webClient(frontServiceProperty));
    }

    //gateway
    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.gate-way-service", name = "enable")
    public ServiceIntegration gateWayServiceIntegration(GateWayServiceProperty gateWayServiceProperty, SslContext sslContext) {
        return new ServiceIntegration(webClient(gateWayServiceProperty, sslContext));
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "integration.gate-way-service", name = "enable")
    public SslContext sslContext() throws SSLException {
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                // modify as you wish, e.g. trust a self-signed cert
                .build();
    }
}
