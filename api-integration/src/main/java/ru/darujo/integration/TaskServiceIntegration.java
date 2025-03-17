package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.dto.TaskDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class TaskServiceIntegration {
    private WebClient webClientTask;

    @Autowired
    public void setWebClientTask(WebClient webClientTask) {
        this.webClientTask = webClientTask;
    }

    public Float getTimeWork(Long workID, String nikName, Date dateGT, Date dateLE) {
        StringBuilder stringBuilder = new StringBuilder();
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
            stringBuilder.append("dateLe=").append(dateToText(dateLE));
        }
        if (dateGT != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("dateGt=").append(dateToText(dateGT));
        }

        System.out.println(stringBuilder);
        try {
            String str = "";
            if (stringBuilder.length() !=0){
                str = "?" + stringBuilder;
            }

            return webClientTask.get().uri("/rep/fact/time" + str)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(Float.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public ListString getListUser(Long workID) {
        try {
            return webClientTask.get().uri("/rep/fact/user?workId=" + workID)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(ListString.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public TaskDto getTask(Long id) {
        return webClientTask.get().uri("/" + id)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Задача c id = " + id + " не найдена")))
                .bodyToMono(TaskDto.class)
                .block();
    }
    public List<Long> getTaskList(String taskDevbo, String taskBts) {
        StringBuilder stringBuilder = new StringBuilder();
        if (taskDevbo != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("codeDEVBO=").append(taskDevbo);
        }
        if (taskBts != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append("codeBTS=").append(taskBts);
        }
        System.out.println(stringBuilder);
        String str = "";
        if (stringBuilder.length() !=0){
            str = "?" + stringBuilder;
        }
        return webClientTask.get().uri( "/list/id" + str)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Задачи не найдены")))
                .bodyToFlux(Long.class).collectList()
                .block();
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date) + "T00:00:00.000Z";
    }

}
