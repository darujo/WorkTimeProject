package ru.darujo.integration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.type.ReportTypeDto;

@Slf4j
@Component
public class InfoServiceIntegration extends ServiceIntegration {
    @Autowired
    public void setWebClient(WebClient webClientInfo) {
        super.setWebClient(webClientInfo);
    }


    public void setMessageTypeListMap(MapUserInfoDto mapUserInfoDto) {
        try {
            webClient.post().uri("/set/types")
                    .bodyValue(mapUserInfoDto)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченному времени"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void addMessage(MessageInfoDto messageInfoDto) {
        try {
            webClient.post().uri("/add/message")
                    .bodyValue(messageInfoDto)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченному времени"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-Information) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void sendReport(@NonNull ReportTypeDto reportType, @NonNull String author, Long chatId, Integer threadId, Integer originMessageId) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "reportType", reportType);
            addTeg(sb, "author", author);
            addTeg(sb, "chatId", chatId);
            addTeg(sb, "threadId", threadId);
            addTeg(sb, "originMessageId", originMessageId);
            webClient.get().uri("/report" + sb)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченному времени"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }


}
