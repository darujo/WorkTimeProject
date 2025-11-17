package ru.darujo.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

@Log4j2
@Component
public class TelegramServiceIntegration extends ServiceIntegration {
    private WebClient webClientTelegram;

    @Autowired
    public void setWebClientTelegram(WebClient webClientTelegram) {
        this.webClientTelegram = webClientTelegram;
    }

    public void sendMessage(
            String author,
            String chatId,
            String text) {
        try {
            webClientTelegram.post().uri("/" + chatId + "/notifications")
                    .header("username", author)
                    .bodyValue(text)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void addFile(
            String fileName,
            String textFile) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "fileName", fileName);
            webClientTelegram.post().uri("/file" + sb)
                    .bodyValue(textFile)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void sendFile(
            String author,
            String chatId,
            String fileName,
            String text) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "fileName", fileName);
            webClientTelegram.post().uri("/" + chatId + "/file" + sb)
                    .header("username", author)
                    .bodyValue(text)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void deleteFile(
            String fileName) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "fileName", fileName);
            webClientTelegram.delete().uri("/file" + sb)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

}
