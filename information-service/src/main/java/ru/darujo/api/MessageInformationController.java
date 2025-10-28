package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.service.MessageInformationService;

import java.sql.Timestamp;

@RestController()
@RequestMapping("/v1/mes_info")
public class MessageInformationController {
    private MessageInformationService messageInformationService;
    @Autowired
    public void setMessageInformation(MessageInformationService messageInformationService) {
        this.messageInformationService = messageInformationService;
    }
    @PostMapping("/add/message")
    public Boolean addMessageInformation(@RequestHeader(required = false) String username,
                                         @RequestBody MessageInfoDto messageInfoDto) {
        if(messageInfoDto.getAuthor() == null && username != null) {
            messageInfoDto.setAuthor(username);
        }
        if(messageInfoDto.getDataTime() == null) {
            messageInfoDto.setDataTime(new Timestamp(System.currentTimeMillis()));
        }
        return messageInformationService.addMessage(messageInfoDto);
    }
    @PostMapping("/set/types")
    public void setMessageTypeListMap(@RequestBody MapUserInfoDto messageTypeListMap){
        messageInformationService.setMessageTypeListMap(messageTypeListMap);
    }


}