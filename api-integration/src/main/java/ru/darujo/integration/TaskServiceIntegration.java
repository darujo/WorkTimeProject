package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.dto.TaskDto;
import ru.darujo.dto.workperiod.UserWorkFormDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.Date;
import java.util.List;


@Component
public class TaskServiceIntegration extends ServiceIntegration {
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
        addTeg( stringBuilder, "workId",workId);
        addTeg( stringBuilder, "nikName",nikName);
        addTeg( stringBuilder, "dateLe",dateLe);
        addTeg( stringBuilder, "dateGt",dateGt);
        addTeg( stringBuilder, "type",type);

        try {
            return webClientTask.get().uri("/rep/fact/time" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToMono(Float.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public ListString getListUser(Long workID,Date dateLe) {
        StringBuilder stringBuilder = new StringBuilder();
        if(workID == null){
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору не задан workId" );
        }
        addTeg(stringBuilder,"workId",workID);
        addTeg(stringBuilder,"dateLe",dateLe);
        try {
            return webClientTask.get().uri("/rep/fact/user" + stringBuilder)
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

    public List<Long> getTaskList(String taskDEVBO, String taskBts) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"codeDEVBO", taskDEVBO);
        addTeg(stringBuilder,"codeBTS", taskBts);
        System.out.println("/list/id" + stringBuilder);

        return webClientTask.get().uri("/list/id" + stringBuilder)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Задачи не найдены")))
                .bodyToFlux(Long.class).collectList()
                .block();
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
    public Boolean setTaskRefreshTime(Long taskId) {
        return setTaskRefreshTime(taskId, null);
    }
    public Boolean setTaskRefreshTime(Long taskId,Date dateWork) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"date", dateWork);
        return webClientTask.get().uri("/refresh/" + taskId + stringBuilder)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Задача c id = " + taskId + " не найдена")))
                .bodyToMono(Boolean.class)
                .block();
    }
    public List<UserWorkFormDto> getWorkUserOrZi(
            Long workId,
            String nikName,
            Boolean addTotal) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "workId", workId);
        addTeg(stringBuilder, "nikName", nikName);
        addTeg(stringBuilder, "addTotal", addTotal);

        try {
            return webClientTask.get().uri("/rep/fact/week" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToFlux(UserWorkFormDto.class)
                    .collectList()
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
}
