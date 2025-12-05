package ru.darujo.integration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.ListString;
import ru.darujo.dto.TaskDto;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.workperiod.UserWorkFormDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Slf4j
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
        addTeg(stringBuilder, "workId", workId);
        addTeg(stringBuilder, "nikName", nikName);
        addTeg(stringBuilder, "dateLe", dateLe);
        addTeg(stringBuilder, "dateGt", dateGt);
        addTeg(stringBuilder, "type", type);

        try {
            return webClientTask.get().uri("/rep/fact/time" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченному времени"))
                    .bodyToMono(Float.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Календарь (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public ListString getListUser(Long workID, Date dateLe) {
        StringBuilder stringBuilder = new StringBuilder();
        if (workID == null) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору не задан workId");
        }
        addTeg(stringBuilder, "workId", workID);
        addTeg(stringBuilder, "dateLe", dateLe);
        try {
            return webClientTask.get().uri("/rep/fact/user" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченному времени"))
                    .bodyToMono(ListString.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Задачи (api-task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public TaskDto getTask(Long id) {
        return webClientTask.get().uri("/" + id)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        cR -> getMessage(cR, "Задача c id = " + id + " не найдена"))
                .bodyToMono(TaskDto.class)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .block();
    }

    public List<Long> getTaskList(String taskDEVBO, String taskBts) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "codeDEVBO", taskDEVBO);
        addTeg(stringBuilder, "codeBTS", taskBts);
        log.info("/list/id{}", stringBuilder);

        return webClientTask.get().uri("/list/id" + stringBuilder)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        cR -> getMessage(cR, "Задачи не найдены"))
                .bodyToFlux(Long.class).collectList()
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .block();
    }

    public Boolean availWorkTime(Long id) {
        try {
            return webClientTask.get().uri("/rep/fact/avail/" + id)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Задача c id = " + id + " не найдена"))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public Boolean setTaskRefreshTime(Long taskId) {
        return setTaskRefreshTime(taskId, null);
    }

    public Boolean setTaskRefreshTime(Long taskId, Date dateWork) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "date", dateWork);
        return webClientTask.get().uri("/refresh/" + taskId + stringBuilder)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        cR -> getMessage(cR, "Задача c id = " + taskId + " не найдена"))
                .bodyToMono(Boolean.class)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .block();
    }

    public List<UserWorkFormDto> getWorkUserOrZi(
            Long workId,
            String nikName,
            Boolean addTotal) throws ResourceNotFoundRunTime {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "workId", workId);
        addTeg(stringBuilder, "nikName", nikName);
        addTeg(stringBuilder, "addTotal", addTotal);

        try {
            return webClientTask.get().uri("/rep/fact/week" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченному времени"))
                    .bodyToFlux(UserWorkFormDto.class)
                    .collectList()
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Timestamp getLastTime(Long workId, Timestamp dateLe, Timestamp dateGe) throws ResourceNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "workId", workId);
        addTeg(stringBuilder, "dateLe", dateLe);
        addTeg(stringBuilder, "dateGe", dateGe);
        try {
            return webClientTask.get().uri("/rep/fact/lastTime" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Задача c id = " + workId + " не найдена"))
                    .bodyToMono(Timestamp.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public List<AttrDto<Integer>> getTaskTypes() {
        try {
            return webClientTask.get().uri("/code/type")
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Не удалось получить справочник TaskType"))
                    .bodyToFlux(new ParameterizedTypeReference<@NonNull AttrDto<Integer>>() {
                    }).collectList()
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.info("getTaskTypes error");
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-Task) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
}
