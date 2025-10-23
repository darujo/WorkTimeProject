package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.Date;


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
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(Void.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
    public void addMessage(MessageInfoDto messageInfoDto) {
        try {
            webClientInfo.post().uri("/add/message")
                    .bodyValue(messageInfoDto)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(Void.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }


}
