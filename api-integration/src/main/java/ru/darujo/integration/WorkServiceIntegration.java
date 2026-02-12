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
    @Autowired
    public void setWebClient(WebClient webClientWork) {
        super.setWebClient(webClientWork);
    }

    public WorkLittleDto getWorEditDto(Long workId) {
        if (workId == null) {
            return new WorkLittleDto();
        }
        try {
            return webClient.get().uri("/obj/little/" + workId)
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

    public MapStringFloat getWorkTimeStageFact(Long workId, Long projectId, Integer stage) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "workId", workId);
        addTeg(stringBuilder, "stage", stage);
        addTeg(stringBuilder, "projectId", projectId);

        try {
            return webClient.get().uri("/rep/time/fact/stage" + stringBuilder)
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

    public Boolean setWorkDate(Long workId, Long projectId, Date dateWork) {
        if (workId != null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "date", dateWork);
            addTeg(stringBuilder, "projectId", projectId);
            return webClient.get().uri("/refresh/" + workId + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "ЗИ c id = " + workId + " не найдена"))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        }
        return false;
    }

    public Boolean getRate(Long workId, Long projectId) {
        Boolean flag = false;
        if (workId != null && projectId != null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "projectId", projectId);
            flag = webClient.get().uri("/rate/" + workId + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "ЗИ c id = " + workId + " не найдена"))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        }
        return flag;
    }

    public void addProject(Long workId, Long projectId) {
        if (workId != null && projectId != null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "projectId", projectId);
            webClient.get().uri("/project/add/" + workId + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "ЗИ c id = " + workId + " не найдена"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        }

    }

    public List<WorkRepDto> getTimeWork(String ziName,
                                        Boolean availWork,
                                        Integer stageZi,
                                        Long releaseId,
                                        Long projectId,
                                        LinkedList<String> sort
                                        ) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "ziName", ziName);
        addTeg(stringBuilder, "availWork", availWork);
        addTeg(stringBuilder, "stageZi", stageZi);
        addTeg(stringBuilder, "releaseId", releaseId);
        addTeg(stringBuilder, "projectId", projectId);
        if (sort != null) {
            sort.forEach(s -> addTeg(stringBuilder, "sort", s));
        }
        try {
            return webClient.get().uri("/rep" + stringBuilder)
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

    public List<WorkUserTime> getWorkUserTime(boolean ziSplit, Long projectId, Timestamp date) {
        return getWorkUserTime(ziSplit,
                null,
                true,
                null,
                date,
                date,
                null,
                projectId,
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
                                              Long projectId,
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
        addTeg(stringBuilder, "projectId", projectId);
        addTeg(stringBuilder, "stageZi", stageZi);
        addTeg(stringBuilder, "codeSap", codeSap);
        addTeg(stringBuilder, "codeZi", codeZi);
        addTeg(stringBuilder, "task", task);
        addTeg(stringBuilder, "releaseId", releaseId);
        addTeg(stringBuilder, "sort", sort);

        try {
            return webClient.get().uri("/rep/fact/week" + stringBuilder)
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
