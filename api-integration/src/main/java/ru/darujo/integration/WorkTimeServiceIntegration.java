package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class WorkTimeServiceIntegration {
    private WebClient webClientWorkTime;

    @Autowired
    public void setWebClientWorkTime(WebClient webClientWorkTime) {
        this.webClientWorkTime = webClientWorkTime;
    }

    public Float getTimeTask(Long taskId, String nikName, Date dateLE, Date dateGT, String type) {
        StringBuilder stringBuilder = new StringBuilder();
        
        addTeg( stringBuilder, "taskId=", taskId);
        addTeg( stringBuilder, "nikName=", nikName);
        addTeg( stringBuilder, "dateLe=", dateToText(dateLE));
        addTeg( stringBuilder, "dateGt=", dateToText(dateGT));
        addTeg( stringBuilder, "type=", type);
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
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить работы (api-worktime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    private void addTeg(StringBuilder stringBuilder, String str, String value) {
        if (value != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(str).append(value);
        }
    }
    private void addTeg(StringBuilder stringBuilder, String str, Long value) {
        if (value != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(str).append(value);
        }
    }

    public ListString getUsers(Long taskId) {
        try {
            return webClientWorkTime.get().uri("/rep/fact/user?taskId=" + taskId)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(ListString.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить работы (api-worktime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date) + "T00:00:00.000Z";
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
