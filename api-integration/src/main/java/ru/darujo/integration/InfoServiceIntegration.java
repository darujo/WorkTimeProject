package ru.darujo.integration;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.type.ReportTypeDto;

@Log4j2
@Component
public class InfoServiceIntegration extends ServiceIntegration {
    private WebClient webClientInfo;

    @Autowired
    public void setWebClientInfo(WebClient webClientInfo) {
        this.webClientInfo = webClientInfo;
    }


    public void setMessageTypeListMap(MapUserInfoDto mapUserInfoDto) {
        try {
            webClientInfo.post().uri("/set/types")
                    .bodyValue(mapUserInfoDto)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void addMessage(MessageInfoDto messageInfoDto) {
        try {
            webClientInfo.post().uri("/add/message")
                    .bodyValue(messageInfoDto)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void sendReport(@NonNull ReportTypeDto reportType, @NonNull String author, Long chatId) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "reportType", reportType);
            addTeg(sb, "author", author);
            addTeg(sb, "chatId", chatId);
            webClientInfo.get().uri("/report" + sb)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }


}
