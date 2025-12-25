package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.model.ChatInfo;
import ru.darujo.service.MessageInformationService;
import ru.darujo.service.ScheduleService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

@RestController()
@RequestMapping("/v1/mes_info")
public class MessageInformationController {
    private MessageInformationService messageInformationService;

    @Autowired
    public void setMessageInformation(MessageInformationService messageInformationService) {
        this.messageInformationService = messageInformationService;
    }

    private ScheduleService scheduleService;

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/add/message")
    public Boolean addMessageInformation(@RequestHeader(required = false) String username,
                                         @RequestBody MessageInfoDto messageInfoDto) {
        if (messageInfoDto.getAuthor() == null && username != null) {
            messageInfoDto.setAuthor(username);
        }
        if (messageInfoDto.getDataTime() == null) {
            messageInfoDto.setDataTime(new Timestamp(System.currentTimeMillis()));
        }
        return messageInformationService.addMessage(messageInfoDto);
    }

    @PostMapping("/set/types")
    public void setMessageTypeListMap(@RequestBody MapUserInfoDto messageTypeListMap) {
        messageInformationService.setMessageTypeListMap(messageTypeListMap);
    }

    @GetMapping("/report")
    public void sendReport(@RequestParam String reportType,
                           @RequestParam String author,
                           @RequestParam(required = false) Long chatId,
                           @RequestParam(required = false) Integer threadId,
                           @RequestParam(required = false) Integer originMessageId) {
        scheduleService.sendReport(reportType, new ChatInfo(author, chatId, threadId, originMessageId));
    }

}