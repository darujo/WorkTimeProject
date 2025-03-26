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

    public Float getTimeWork(Long workId, String nikName, Date dateGt, Date dateLe) {
        return getTimeWork(workId, nikName, dateGt, dateLe, null);
    }

    public Float getTimeWork(Long workId, String nikName, Date dateGt, Date dateLe, String type) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg( stringBuilder, "workId=",workId);
        addTeg( stringBuilder, "nikName=",nikName);
        addTeg( stringBuilder, "dateLe=",dateLe);
        addTeg( stringBuilder, "dateGt=",dateGt);
        addTeg( stringBuilder, "type=",type);
        String str = "";
        if (stringBuilder.length() != 0) {
            str = "?" + stringBuilder;
        }
        System.out.println("/rep/fact/time" + str);
        try {
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
    private void addTeg(StringBuilder stringBuilder, String str, Date value) {
        addTeg(stringBuilder,str,dateToText(value));
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
        String str = "";
        if (stringBuilder.length() != 0) {
            str = "?" + stringBuilder;
        }
        System.out.println("/list/id" + str);

        return webClientTask.get().uri("/list/id" + str)
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

    public Boolean availWorkTime(Long id) {
        try {
            return webClientTask.get().uri("/rep/fact/avail/" + id)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Задача c id = " + id + " не найдена")))
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
