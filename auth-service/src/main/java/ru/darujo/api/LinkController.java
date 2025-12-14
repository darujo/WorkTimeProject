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
                                            @RequestParam(required = false) String messageType) {
        return linkService.getGenSingleCode(nikName == null ? username : nikName, messageType);
    }

    @GetMapping("/get/{chatId}")
    public ResultMes checkUserTelegram(@PathVariable(required = false) Long chatId) {
        return linkService.checkUserTelegram(chatId);
    }

    @GetMapping("/link")
    public ResultMes linkSingleCode(@RequestParam Integer code,
                                    @RequestParam Long telegramId,
                                    @RequestParam(required = false) Integer threadId
    ) {
        return linkService.linkCodeTelegram(code, telegramId, threadId);
    }

    @GetMapping("/delete")
    public void linkDeleteTelegram(@RequestParam Long telegramId,
                                   @RequestParam(required = false) Integer threadId
    ) {
        linkService.linkDeleteTelegram(telegramId,threadId);
    }

    @GetMapping("/delete/type")
    public void linkDeleteTelegramType(@RequestParam String nikName,
                                       @RequestParam(required = false) String messageType
    ) {
        linkService.linkDeleteTelegram(nikName, messageType);
    }

}
