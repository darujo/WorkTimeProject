package ru.darujo.telegram_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.max.botapi.core.PollingErrorHandler;
import ru.max.botapi.core.UpdateHandler;
import ru.max.botapi.model.MessageCreatedUpdate;

@Slf4j
@Component
public class MaxBotConfig {

    @Bean
    UpdateHandler updateHandler() {
        return update -> {
            if (update instanceof MessageCreatedUpdate msg) {
                // обработать сообщение
            }
        };
    }
    @Bean
    PollingErrorHandler pollingErrorHandler() {
        return e -> log.error("Ошибка опроса", e);
    }
}
