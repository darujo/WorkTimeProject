package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.Date;


@Component
public class WorkTimeServiceIntegration {
    private WebClient webClientWorkTime;

    @Autowired
    public void setWebClientWorkTime(WebClient webClientWorkTime) {
        this.webClientWorkTime = webClientWorkTime;
    }

    public Float getTimeTask(Long taskId, String userName, Date dateLE, Date dateGT) {
        StringBuilder stringBuilder = new StringBuilder();
        if (stringBuilder.length() == 0 ){
            stringBuilder.append("?");
        }
        if (taskId != null) {
            if (stringBuilder.length() != 0){
                stringBuilder.append("&");
            }
            stringBuilder.append("taskId=").append(taskId);
        }
        if (userName != null) {
            if (stringBuilder.length() != 0){
                stringBuilder.append("&");
            }
            stringBuilder.append("userName=").append(userName);
        }
        if (dateLE != null) {
            if (stringBuilder.length() != 0){
                stringBuilder.append("&");
            }
            stringBuilder.append("dateLe=").append(dateLE);
        }
        if (dateGT != null) {
            if (stringBuilder.length() != 0){
                stringBuilder.append("&");
            }
            stringBuilder.append("dateGt=").append(dateGT);
        }

        System.out.println(stringBuilder);
        return webClientWorkTime.get().uri("/rep/time" + stringBuilder)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                .bodyToMono(Float.class)
                .block();
    }
}
