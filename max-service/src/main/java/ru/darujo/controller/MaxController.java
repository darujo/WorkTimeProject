package ru.darujo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.darujo.max_bot.MaxBotSend;
import ru.darujo.model.ChatInfo;
import ru.darujo.service.FileSaverService;

import java.io.File;

@Slf4j
@RestController
@RequestMapping("v1/${app.http.bot}")
@SuppressWarnings("unused")
public class MaxController {

    private MaxBotSend maxBotSend;

    @Autowired
    public void setMaxBotSend(MaxBotSend maxBotSend) {
        this.maxBotSend = maxBotSend;
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
                                      @RequestParam(required = false) String originMessageId,
                                      @RequestBody String text)  {
        log.debug("/notifications");
        maxBotSend.sendMessage(new ChatInfo(username, chatId, originMessageId), text);
    }

    @PostMapping(value = "/send/admin")
    public void sendMessageToTelegram(@RequestBody MessageAdmin message) {
        log.debug("/send/admin");
        maxBotSend.sendMessageForAdmin(message);
    }

    @PostMapping(value = "/file")
    public String addFile(@RequestParam String fileName,
                          @RequestBody byte[] body) {
        log.debug("/file");
        return fileSaverService.addFile(fileName, body);
    }

    @PostMapping(value = "/{chatId}/file")
    public void sendFile(@RequestHeader String username,
                         @PathVariable String chatId,
                         @RequestParam(required = false) Integer threadId,
                         @RequestParam(required = false) String originMessageId,
                         @RequestParam String fileName,
                         @RequestBody String text)  {
        log.debug("chat/file");
        File file = fileSaverService.getFile(fileName);
        maxBotSend.sendDocument(new ChatInfo(username, chatId, originMessageId), fileName, file, text);
    }

    @DeleteMapping(value = "/file")
    public void deleteFile(@RequestParam String fileName) {
        log.debug("del /file");
        fileSaverService.delFile(fileName);
    }
}



