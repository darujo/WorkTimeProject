package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.information.CodeTelegramMes;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.service.LinkService;

@RestController
@CrossOrigin
@RequestMapping("/users/user/telegram")
public class LinkController {
    LinkService linkService;

    @Autowired
    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/get")
    public CodeTelegramMes getGenSingleCode(@RequestHeader String username,
                                            @RequestParam(required = false) String nikName,
                                            @RequestParam(required = false) String messageType,
                                            @RequestParam(required = false) Long projectId) {
        return linkService.getGenSingleCode(nikName == null ? username : nikName, messageType, projectId);
    }

    @GetMapping("/get/{chatId}")
    public ResultMes checkUserTelegram(@RequestParam String senderType,
                                       @PathVariable(required = false) String chatId) {
        return linkService.checkUserTelegram(senderType, chatId);
    }

    @GetMapping("/link")
    public ResultMes linkSingleCode(@RequestParam Integer code,
                                    @RequestParam String senderType,
                                    @RequestParam String telegramId,
                                    @RequestParam(required = false) Integer threadId
    ) {
        return linkService.linkCodeTelegram(code, senderType, telegramId, threadId);
    }

    @GetMapping("/delete")
    public void linkDeleteTelegram(@RequestParam String senderType,
                                   @RequestParam String telegramId,
                                   @RequestParam(required = false) Integer threadId
    ) {
        linkService.linkDeleteTelegram(senderType, telegramId, threadId);
    }

    @GetMapping("/delete/type")
    public void linkDeleteTelegramType(@RequestParam String senderType,
                                       @RequestHeader String username,
                                       @RequestParam(required = false) String messageType
    ) {
        linkService.linkDeleteTelegram(username, senderType, messageType);
    }

}
