package ru.darujo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.telegram_bot.TelegramBot;


@RestController()
@RequestMapping("/v1/bot")
@Log4j2
public class SystemController implements ApplicationContextAware {
    private TelegramBot telegramBot;

    @Autowired
    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @GetMapping("/test")
    public void test() {}
    private ApplicationContext context;
    @PostMapping("/shutdownContext")
    public void shutdownContext() {
        telegramBot.close();
        ((ConfigurableApplicationContext) context).close();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;

    }}
