package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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



    @PostMapping("")
    public Boolean addMessageInformation(@RequestBody WorkCriteriaDto workCriteriaDto) {
        return true;
    }


}