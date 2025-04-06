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
public class WorkTimeServiceIntegration extends ServiceIntegration{
    private WebClient webClientWorkTime;

    @Autowired
    public void setWebClientWorkTime(WebClient webClientWorkTime) {
        this.webClientWorkTime = webClientWorkTime;
    }

    public Float getTimeTask(Long taskId, String nikName, Date dateLE, Date dateGT, String type) {
        StringBuilder stringBuilder = new StringBuilder();
        
        addTeg( stringBuilder, "taskId", taskId);
        addTeg( stringBuilder, "nikName", nikName);
        addTeg( stringBuilder, "dateLe", dateToText(dateLE));
        addTeg( stringBuilder, "dateGt", dateToText(dateGT));
        addTeg( stringBuilder, "type", type);
        String str = "";
        if (stringBuilder.length() != 0) {
            str = "?" + stringBuilder;
        }
        System.out.println("/rep/fact/time" + str);
        
        try {
            return webClientWorkTime.get().uri("/rep/fact/time" + str)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(Float.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public ListString getUsers(Long taskId,Date dateLe) {
        StringBuilder stringBuilder = new StringBuilder();

        addTeg( stringBuilder, "taskId", taskId);
        addTeg( stringBuilder, "dateLe", dateToText(dateLe));

        try {
            return webClientWorkTime.get().uri("/rep/fact/user?" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(ListString.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Boolean availTime(Long taskId) {
        try {
            Boolean b
             = webClientWorkTime.get().uri("/rep/fact/availTime/" + taskId)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(Boolean.class)
                    .block();
            return b;
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
