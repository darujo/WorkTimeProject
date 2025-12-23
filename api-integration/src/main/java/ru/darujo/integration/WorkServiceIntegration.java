package ru.darujo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@Component
public class WorkServiceIntegration extends ServiceIntegration {
    private WebClient webClientWork;

    @Autowired
    public void setWebClientWork(WebClient webClientWork) {
        this.webClientWork = webClientWork;
    }

    public WorkLittleDto getWorEditDto(Long workId) {
        if (workId == null) {
            return new WorkLittleDto();
        }
        try {
            return webClientWork.get().uri("/obj/little/" + workId)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по ЗИ с ID = " + workId))
                    .bodyToMono(WorkLittleDto.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error("/obj/little/{}", workId);
            log.error(ex.getMessage());
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public MapStringFloat getWorkTimeStageFact(Long workId, Integer stage) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "workId", workId);
        addTeg(stringBuilder, "stage", stage);

        try {
            return webClientWork.get().uri("/rep/time/fact/stage" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по ЗИ с ID = " + workId))
                    .bodyToMono(MapStringFloat.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error("/rep/time/fact/stage0{}", stringBuilder);
            log.error(ex.getMessage());
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Boolean setWorkDate(Long workId, Date dateWork) {
        if (workId != null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "date", dateWork);
            return webClientWork.get().uri("/refresh/" + workId + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "ЗИ c id = " + workId + " не найдена"))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        }
        return false;
    }

    public List<WorkRepDto> getTimeWork(String ziName,
                                        Boolean availWork,
                                        Integer stageZi,
                                        Long releaseId,
                                        LinkedList<String> sort) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "ziName", ziName);
        addTeg(stringBuilder, "availWork", availWork);
        addTeg(stringBuilder, "stageZi", stageZi);
        addTeg(stringBuilder, "releaseId", releaseId);
        if (sort != null) {
            sort.forEach(s -> addTeg(stringBuilder, "sort", s));
        }
        try {
            return webClientWork.get().uri("/rep" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по ЗИ"))
                    .bodyToFlux(WorkRepDto.class)
                    .collectList()
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error("/rep{}", stringBuilder);
            log.error(ex.getMessage());
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public List<WorkUserTime> getWorkUserTime(boolean ziSplit, Timestamp date) {
        return getWorkUserTime(ziSplit,
                null,
                true,
                null,
                date,
                date,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public List<WorkUserTime> getWorkUserTime(boolean ziSplit,
                                              String nikName,
                                              Boolean addTotal,
                                              Boolean weekSplit,

                                              Timestamp dateStart,
                                              Timestamp dateEnd,

                                              String name,
                                              Integer stageZi,
                                              Long codeSap,
                                              String codeZi,
                                              String task,
                                              Long releaseId,

                                              String sort) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "ziSplit", ziSplit);
        addTeg(stringBuilder, "nikName", nikName);
        addTeg(stringBuilder, "addTotal", addTotal);
        addTeg(stringBuilder, "weekSplit", weekSplit);
        addTeg(stringBuilder, "dateStart", dateStart);
        addTeg(stringBuilder, "dateEnd", dateEnd);
        addTeg(stringBuilder, "weekSplit", weekSplit);

        addTeg(stringBuilder, "name", name);
        addTeg(stringBuilder, "stageZi", stageZi);
        addTeg(stringBuilder, "codeSap", codeSap);
        addTeg(stringBuilder, "codeZi", codeZi);
        addTeg(stringBuilder, "task", task);
        addTeg(stringBuilder, "releaseId", releaseId);
        addTeg(stringBuilder, "sort", sort);

        try {
            return webClientWork.get().uri("/rep/fact/week" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по ЗИ"))
                    .bodyToFlux(WorkUserTime.class)
                    .collectList()
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error("/rep/fact/week{}", stringBuilder);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
}
