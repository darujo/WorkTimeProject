package ru.darujo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.telegram_bot.TelegramBot;


@RestController()
@RequestMapping("/v1/bot")
@Log4j2
public class SystemController {
    //    private TelegramBot telegramBot;
//
//    @Autowired
//    public void setTelegramBot(TelegramBot telegramBot) {
//        this.telegramBot = telegramBot;
//    }
    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/test")
    public void test() {
    }

    @PostMapping("/shutdownContext")
    @Async
    public void shutdownContext() {
//        telegramBot.close();
        SpringApplication.exit(applicationContext);
//        ((ConfigurableApplicationContext) context).close();
    }

}
