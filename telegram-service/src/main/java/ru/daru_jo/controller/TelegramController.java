package ru.daru_jo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.daru_jo.telegram_bot.TelegramBotSend;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс UsersController - простенький контроллер для получения списка мыслей из БД
 *
 * @author MaxIvanov
 * created 13.07.2021
 */
@RestController
@RequestMapping("${app.http.bot}")
@SuppressWarnings("unused")
public class TelegramController {

    private TelegramBotSend telegramBotSend;

    @Autowired
    public void setTelegramBotSend(TelegramBotSend telegramBotSend) {
        this.telegramBotSend = telegramBotSend;
    }


    @PostMapping(value = "/{userId}/notifications", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void sendMessageToTelegram(@PathVariable String userId, @RequestBody String text) throws TelegramApiException {
        telegramBotSend.sendMessage(userId,text);
    }
}



