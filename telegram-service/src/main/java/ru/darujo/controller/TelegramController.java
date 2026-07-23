package ru.darujo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.dto.information.SendAdminMessage;
import ru.darujo.model.ChatInfo;
import ru.darujo.service.FileSaverService;
import ru.darujo.telegram_bot.TelegramBotSend;

import java.io.File;

@RestController
@RequestMapping("v1/${app.http.bot}")
@SuppressWarnings("unused")
public class TelegramController {

    private TelegramBotSend telegramBotSend;

    @Autowired
    public void setTelegramBotSend(TelegramBotSend telegramBotSend) {
        this.telegramBotSend = telegramBotSend;
    }

    private FileSaverService fileSaverService;

    @Autowired
    public void setFileService(FileSaverService fileSaverService) {
        this.fileSaverService = fileSaverService;
    }

    @PostMapping(value = "/{chatId}/notifications", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void sendMessageToTelegram(@RequestHeader String username,
                                      @PathVariable String chatId,
                                      @RequestParam(required = false) Integer threadId,
                                      @RequestParam(required = false) Integer originMessageId,
                                      @RequestBody String text) throws TelegramApiException {
        telegramBotSend.sendMessage(new ChatInfo(username, chatId, threadId, originMessageId), text);
    }

    @PostMapping(value = "/send/admin")
    public void sendMessageToTelegram(@RequestBody SendAdminMessage message) throws TelegramApiException {
        telegramBotSend.sendMessageForAdmin(message);
    }

    @PostMapping(value = "/file")
    public String addFile(@RequestParam String fileName,
                          @RequestBody byte[] body) {
        return fileSaverService.addFile(fileName, body);
    }

    @PostMapping(value = "/{chatId}/file")
    public void sendFile(@RequestHeader String username,
                         @PathVariable String chatId,
                         @RequestParam(required = false) Integer threadId,
                         @RequestParam(required = false) Integer originMessageId,
                         @RequestParam String fileName,
                         @RequestBody String text) throws TelegramApiException {
        File file = fileSaverService.getFile(fileName);
        telegramBotSend.sendDocument(new ChatInfo(username, chatId, threadId, originMessageId), fileName, file, text);
    }

    @DeleteMapping(value = "/file")
    public void deleteFile(@RequestParam String fileName) {
        fileSaverService.delFile(fileName);
    }
}



