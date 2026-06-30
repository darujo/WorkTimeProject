package ru.darujo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.darujo.max_bot.MaxBotSend;
import ru.darujo.model.ChatInfo;
import ru.darujo.service.FileService;

import java.io.File;

@RestController
@RequestMapping("v1/${app.http.bot}")
@SuppressWarnings("unused")
public class MaxController {

    private MaxBotSend maxBotSend;

    @Autowired
    public void setMaxBotSend(MaxBotSend maxBotSend) {
        this.maxBotSend = maxBotSend;
    }

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/{chatId}/notifications", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void sendMessageToTelegram(@RequestHeader String username,
                                      @PathVariable String chatId,
                                      @RequestParam(required = false) Integer threadId,
                                      @RequestParam(required = false) String originMessageId,
                                      @RequestBody String text)  {
        maxBotSend.sendMessage(new ChatInfo(username, chatId, originMessageId), text);
    }

    @PostMapping(value = "/send/admin")
    public void sendMessageToTelegram(@RequestBody MessageAdmin message) {
        maxBotSend.sendMessageForAdmin(message);
    }

    @PostMapping(value = "/file")
    public String addFile(@RequestParam String fileName,
                          @RequestBody byte[] body) {
        return fileService.addFile(fileName, body);
    }

    @PostMapping(value = "/{chatId}/file")
    public void sendFile(@RequestHeader String username,
                         @PathVariable String chatId,
                         @RequestParam(required = false) Integer threadId,
                         @RequestParam(required = false) String originMessageId,
                         @RequestParam String fileName,
                         @RequestBody String text)  {
        File file = fileService.getFile(fileName);
        maxBotSend.sendDocument(new ChatInfo(username, chatId, originMessageId), fileName, file, text);
    }

    @DeleteMapping(value = "/file")
    public void deleteFile(@RequestParam String fileName) {
        fileService.delFile(fileName);
    }
}



