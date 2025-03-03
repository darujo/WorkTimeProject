package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


@Component
public class TaskServiceIntegration {
    private WebClient webClientWork;

    @Autowired
    public void setWebClientWork(WebClient webClientWork) {
        this.webClientWork = webClientWork;
    }

    public Float getTimeWork(Long workID, String userName, Date dateGT, Date dateLE) {
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
        if (userName != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("userName=").append(userName);
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
        return webClientWork.get().uri("/rep/fact/time" + stringBuilder)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                .bodyToMono(Float.class)
                .block();
    }

    public ListString getListUser(Long workID) {
        return webClientWork.get().uri("/rep/fact/user?workId=" + workID)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                .bodyToMono(ListString.class)
                .block();

    }

}
