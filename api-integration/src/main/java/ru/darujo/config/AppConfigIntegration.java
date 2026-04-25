package ru.darujo.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
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

@Component
public class AppConfigIntegration extends WebClientConfig {
    // Telegram
    @Bean(name = "telegramServiceIntegration")

    @ConditionalOnBooleanProperty(prefix = "integration.telegram-service", name = "enable")
    public TelegramServiceIntegrationImp telegramServiceIntegration(TelegramServiceProperty telegramServiceProperty) {
        return new TelegramServiceIntegrationImp(webClient(telegramServiceProperty));
    }

    // Calendar
    @Bean(name = "calendarServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.calendar-service", name = "enable")
    public CalendarServiceIntegrationImp calendarServiceIntegration(CalendarServiceProperty calendarServiceProperty) {
        return new CalendarServiceIntegrationImp(webClient(calendarServiceProperty));
    }

    //Information
    @Bean(name = "infoServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.information-service", name = "enable")
    public InfoServiceIntegrationImp infoServiceIntegration(InfoServiceProperty infoServiceProperty) {
        return new InfoServiceIntegrationImp(webClient(infoServiceProperty));
    }

    //Rate
    @Bean(name = "rateServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.rate-service", name = "enable")
    public RateServiceIntegrationImp rateServiceIntegration(RateServiceProperty rateServiceProperty) {
        return new RateServiceIntegrationImp(webClient(rateServiceProperty));
    }

    //Task
    @Bean(name = "taskServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.task-service", name = "enable")
    public TaskServiceIntegrationImp taskServiceIntegration(TaskServiceProperty taskServiceProperty) {
        return new TaskServiceIntegrationImp(webClient(taskServiceProperty));
    }

    //User
    @Bean(name = "userServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.user-service", name = "enable")
    public UserServiceIntegrationImp userServiceIntegration(UserServiceProperty userServiceProperty) {
        return new UserServiceIntegrationImp(webClient(userServiceProperty));
    }

    //Work
    @Bean(name = "workServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.work-service", name = "enable")
    public WorkServiceIntegrationImp workServiceIntegration(WorkServiceProperty workServiceProperty) {
        return new WorkServiceIntegrationImp(webClient(workServiceProperty));
    }

    //WorkTime
    @Bean(name = "workTimeServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.work-time-service", name = "enable")
    public WorkTimeServiceIntegrationImp workTimeServiceIntegration(WorkTimeServiceProperty workTimeServiceProperty) {
        return new WorkTimeServiceIntegrationImp(webClient(workTimeServiceProperty));
    }

    //Front
    @Bean(name = "frontServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.front-service", name = "enable")
    public ServiceIntegrationImp frontServiceIntegration(FrontServiceProperty frontServiceProperty) {
        return new ServiceIntegrationImp(webClient(frontServiceProperty));
    }

    //gateway
    @Bean(name = "gateWayServiceIntegration")
    @ConditionalOnBooleanProperty(prefix = "integration.gate-way-service", name = "enable")
    public ServiceIntegrationImp gateWayServiceIntegration(GateWayServiceProperty gateWayServiceProperty, SslContext sslContext) {
        return new ServiceIntegrationImp(webClient(gateWayServiceProperty, sslContext));
    }

    @Bean("sslContext")
    @ConditionalOnBooleanProperty(prefix = "integration.gate-way-service", name = "enable")
    public SslContext sslContext() throws SSLException {
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                // modify as you wish, e.g. trust a self-signed cert
                .build();
    }
}
