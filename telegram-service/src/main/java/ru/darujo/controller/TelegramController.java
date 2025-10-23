package ru.darujo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.telegram_bot.TelegramBotSend;

@RestController
@RequestMapping("$v1/{app.http.bot}")
@SuppressWarnings("unused")
public class TelegramController {

    private TelegramBotSend telegramBotSend;

    @Autowired
    public void setTelegramBotSend(TelegramBotSend telegramBotSend) {
        this.telegramBotSend = telegramBotSend;
    }


    @PostMapping(value = "/{chatId}/notifications", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void sendMessageToTelegram(@RequestHeader String username,
                                      @PathVariable String chatId,
                                      @RequestBody String text, @PathVariable String app) throws TelegramApiException {
        telegramBotSend.sendMessage(username, chatId, text);
    }
}



