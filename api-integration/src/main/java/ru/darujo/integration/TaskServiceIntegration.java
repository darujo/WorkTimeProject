package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.dto.WorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.Date;


@Component
public class TaskServiceIntegration {
    private WebClient webClientTask;

    @Autowired
    public void setWebClientTask(WebClient webClientTask) {
        this.webClientTask = webClientTask;
    }

    public Float getTimeWork(Long workID, String nikName, Date dateGT, Date dateLE) {
        StringBuilder stringBuilder = new StringBuilder();
        if (stringBuilder.length() == 0) {
            stringBuilder.append("?");
        }
        if (workID != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("workId=").append(workID);
        }
        if (nikName != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("nikName=").append(nikName);
        }
        if (dateLE != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("dateLe=").append(dateLE);
        }
        if (dateGT != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("dateGt=").append(dateGT);
        }

        System.out.println(stringBuilder);
        return webClientTask.get().uri("/rep/fact/time" + stringBuilder)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                .bodyToMono(Float.class)
                .block();
    }

    public ListString getListUser(Long workID) {
        return webClientTask.get().uri("/rep/fact/user?workId=" + workID)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                .bodyToMono(ListString.class)
                .block();

    }
    public WorkDto getWork(Long id) {
        return webClientTask.get().uri("/" + id)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Задача c id = " + id + " не найдена")))
                .bodyToMono(WorkDto.class)
                .block();
    }

}
