package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.ratestage.WorkCriteriaDto;
import ru.darujo.service.MessageInformationService;

@RestController()
@RequestMapping("/v1/mes_info")
public class MessageInformationController {
    private MessageInformationService messageInformationService;
    @Autowired
    public void setMessageInformation(MessageInformationService messageInformationService) {
        this.messageInformationService = messageInformationService;
    }



    @PostMapping("/add/message")
    public Boolean addMessageInformation(@RequestBody MessageInfoDto messageInfoDto) {
        return messageInformationService.addMessage(messageInfoDto);
    }
    @PostMapping("/set/types")
    public void setMessageTypeListMap(@RequestBody MapUserInfoDto messageTypeListMap){
        messageInformationService.setMessageTypeListMap(messageTypeListMap);
    }


}