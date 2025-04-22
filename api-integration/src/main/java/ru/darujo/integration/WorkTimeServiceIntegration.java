package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.dto.workperiod.UserWorkFormDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.Date;
import java.util.List;


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

    public ListString getUsers(Long taskId, Date dateLe) {
        StringBuilder stringBuilder = new StringBuilder();

        addTeg(stringBuilder, "taskId", taskId);
        addTeg(stringBuilder, "dateLe", dateLe);

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
        addTeg(stringBuilder, "addTotal", addTotal);
        addTeg(stringBuilder, "weekSplit", weekSplit);
        addTeg(stringBuilder, "dateStart", dateStart);
        addTeg(stringBuilder, "dateEnd", dateEnd);

        try {
            return webClientWorkTime.get().uri("/rep/fact/week?" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                    .bodyToFlux(UserWorkDto.class)
                    .collectList()
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
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
            return webClientWorkTime.get().uri("/rep/fact/week?" + stringBuilder)
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
