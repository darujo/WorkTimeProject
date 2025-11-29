package ru.darujo.telegram_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Slf4j
@Component
@SuppressWarnings("unused")
public class TelegramBot {
    @Autowired
    public TelegramBot(@Value("${telegram-bot.token}") String botToken,
                       TelegramBotRequest telegramBotRequest) {
        try {
            // нельзя оборачивать в try так как отвалится процесс Приема сообщений
            @SuppressWarnings("resource")
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

            botsApplication.registerBot(botToken, telegramBotRequest);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

        }

    }


}
