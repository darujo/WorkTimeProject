package ru.darujo.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.dto.workperiod.UserWorkFormDto;
import ru.darujo.dto.workperiod.WorkUserFactPlan;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Log4j2
@Component
public class WorkTimeServiceIntegration extends ServiceIntegration {
    private WebClient webClientWorkTime;

    @Autowired
    public void setWebClientWorkTime(WebClient webClientWorkTime) {
        this.webClientWorkTime = webClientWorkTime;
    }

    public Float getTimeTask(Long taskId, String nikName, Date dateLE, Date dateGT, String type) {
        StringBuilder stringBuilder = new StringBuilder();

        addTeg(stringBuilder, "taskId", taskId);
        addTeg(stringBuilder, "nikName", nikName);
        addTeg(stringBuilder, "type", type);
        addTeg(stringBuilder, "dateLe", dateLE);
        addTeg(stringBuilder, "dateGt", dateGT);


        try {
            return webClientWorkTime.get().uri("/rep/fact/time" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени"))
                    .bodyToMono(Float.class)
                    .block();
        } catch (RuntimeException ex) {
            log.error("/rep/fact/time{}", stringBuilder);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public ListString getUsers(Long taskId, Date dateLe) {
        StringBuilder stringBuilder = new StringBuilder();

        addTeg(stringBuilder, "taskId", taskId);
        addTeg(stringBuilder, "dateLe", dateLe);

        try {
            return webClientWorkTime.get().uri("/rep/fact/user" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени"))
                    .bodyToMono(ListString.class)
                    .block();
        } catch (RuntimeException ex) {
            log.error("/rep/fact/user{}", stringBuilder);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Boolean availTime(Long taskId) {
        try {
            return webClientWorkTime.get().uri("/rep/fact/availTime/" + taskId)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени"))
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public List<UserWorkDto> getWorkUserOrZi(
            Iterable<Long> taskIds,
            String nikName,
            Boolean addTotal,
            Boolean weekSplit,
            Date dateStart,
            Date dateEnd) {
        StringBuilder stringBuilder = new StringBuilder();
        taskIds.forEach(taskId -> addTeg(stringBuilder, "taskId", taskId));

        addTeg(stringBuilder, "nikName", nikName);
        if(stringBuilder.isEmpty()){
            log.error("нет тасков");
            return null;

        }addTeg(stringBuilder, "addTotal", addTotal);
        addTeg(stringBuilder, "weekSplit", weekSplit);
        addTeg(stringBuilder, "dateStart", dateStart);
        addTeg(stringBuilder, "dateEnd", dateEnd);

        try {
            return webClientWorkTime.get().uri("/rep/fact/week" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченному времени"))
                    .bodyToFlux(UserWorkDto.class)
                    .collectList()
                    .block();
        } catch (RuntimeException ex) {
            log.error("/rep/fact/week{}", stringBuilder);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public List<UserWorkFormDto> getWorkUserOrZiBig(Long taskId,
                                                    String nikName,
                                                    Boolean addTotal,
                                                    Boolean weekSplit,
                                                    Date dateStart,
                                                    Date dateEnd) {
        StringBuilder stringBuilder = new StringBuilder();

        addTeg(stringBuilder, "taskId", taskId);
        addTeg(stringBuilder, "nikName", nikName);
        addTeg(stringBuilder, "addTotal", addTotal);
        addTeg(stringBuilder, "weekSplit", weekSplit);
        addTeg(stringBuilder, "dateStart", dateStart);
        addTeg(stringBuilder, "dateEnd", dateEnd);

        try {
            return webClientWorkTime.get().uri("/rep/fact/week" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени"))
                    .bodyToFlux(UserWorkFormDto.class)
                    .collectList()
                    .block();
        } catch (RuntimeException ex) {
            log.error("getWorkUserOrZiBig /rep/fact/week{}", stringBuilder);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Timestamp getLastTime(List<Long> taskIds, Timestamp dateLe, Timestamp dateGe) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            taskIds.forEach(taskId -> addTeg(stringBuilder, "taskId", taskId));
            addTeg(stringBuilder, "dateLe", dateLe);
            addTeg(stringBuilder, "dateGe", dateGe);

            return webClientWorkTime.get().uri("/rep/fact/lastTime" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени. Статус "))
                    .bodyToMono(Timestamp.class)
                    .block();
        } catch (RuntimeException ex) {
            log.error("/rep/fact/lastTime{}", stringBuilder);
            log.error(ex.getMessage());
            return null;
        }

    }

    public WorkUserFactPlan getUserWork(Timestamp dateStart, Timestamp dateEnd, String nikName, String period) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            addTeg(stringBuilder, "dateStart", dateStart);
            addTeg(stringBuilder, "dateEnd", dateEnd);
            addTeg(stringBuilder, "nikName", nikName);
            addTeg(stringBuilder, "periodSplit", period);

            return webClientWorkTime.get().uri("/rep/fact/user/work/only" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по затраченому времени. Статус"))
                    .bodyToMono(WorkUserFactPlan.class)
                    .block();
        } catch (RuntimeException ex) {
            log.error("rep/fact/user/work/only{}", stringBuilder);
            log.error(ex.getMessage());
            return null;
        }

    }
}
