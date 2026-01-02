package ru.darujo.telegram_bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//@Slf4j
@Component
public class TelegramBotConfig {
    @Bean(name = "telegramScheduledExecutorService", defaultCandidate = false) // <-- 2
    public ScheduledExecutorService telegramScheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean // <-- 3
    public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication(
            @Qualifier("telegramScheduledExecutorService") ScheduledExecutorService scheduledExecutorService // <-- 4
    ) {
        return new TelegramBotsLongPollingApplication(
                ObjectMapper::new, // <-- 5
                new TelegramOkHttpClientFactory.DefaultOkHttpClientCreator(), // <-- 6
                () -> scheduledExecutorService
        );

    }
}
