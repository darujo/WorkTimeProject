package ru.daru_jo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс WebHookApp
 *
 * @author MaxIvanov
 * created 20.07.2021
 */


@SpringBootApplication
public class TelegramApp {
    public static void main(String[] args) {
        SpringApplication.run(TelegramApp.class, args);
    }
}
