package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.Date;


@Component
public class InfoServiceIntegration extends ServiceIntegration {
    private WebClient webClientInfo;

    @Autowired
    public void setWebClientInfo(WebClient webClientInfo) {
        this.webClientInfo = webClientInfo;
    }



    public ListString getListUser(Long workID,Date dateLe) {
        StringBuilder stringBuilder = new StringBuilder();
        if(workID == null){
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору не задан workId" );
        }
        addTeg(stringBuilder,"workId",workID);
        addTeg(stringBuilder,"dateLe",dateLe);
        try {
            return webClientInfo.get().uri("/rep/fact/user" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(ListString.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }


}
