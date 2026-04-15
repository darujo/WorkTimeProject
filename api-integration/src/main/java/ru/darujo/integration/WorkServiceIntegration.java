package ru.darujo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class WorkServiceIntegration extends ServiceIntegration {
    public WorkServiceIntegration(WebClient webClientWork) {
        super.setWebClient(webClientWork);
    }

    public WorkLittleDto getWorEditDto(Long workId) {
        if (workId == null) {
            return new WorkLittleDto();
        }
        String uri = "/obj/little/" + workId;
        try {
            return webClient.get().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по ЗИ с ID = " + workId))
                    .bodyToMono(WorkLittleDto.class)
                    .doOnError(throwable -> log.error(uri, throwable))
                    .block();
        } catch (RuntimeException ex) {
            log.error(uri, ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public List<Long> workLittleIdList(String name,
                                       Integer stageZi,
                                       Long projectId,
                                       Long codeSap,
                                       String task,
                                       String codeZi) {
        StringBuilder sb = new StringBuilder();
        addTeg(sb, "name", name);
        addTeg(sb, "stageZi", stageZi);
        addTeg(sb, "projectId", projectId);
        addTeg(sb, "codeSap", codeSap);
        addTeg(sb, "task", task);
        addTeg(sb, "codeZi", codeZi);
        String uri = "/obj/little/id" + sb;
        try {
            return webClient.get().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, uri + " Что-то пошло не так не удалось получить данные по ЗИ "))
                    .bodyToFlux(Long.class)
                    .doOnError(throwable -> log.error(uri, throwable))
                    .collectList().block();
        } catch (RuntimeException ex) {
            log.error(uri, ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }

    }


    public MapStringFloat getWorkTimeStageFact(List<Long> workId, Long projectId, Integer stage) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "workId", workId);
        addTeg(stringBuilder, "stage", stage);
        addTeg(stringBuilder, "projectId", projectId);
        String uri = "/rep/time/fact/stage" + stringBuilder;
        try {
            return webClient.get().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, uri + " Что-то пошло не так не удалось получить данные по ЗИ с ID = " + workId))
                    .bodyToMono(MapStringFloat.class)
                    .doOnError(throwable -> log.error(uri, throwable))
                    .block();
        } catch (RuntimeException ex) {
            log.error(uri, ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Boolean setWorkDate(Long workId, Long projectId, Date dateWork) {
        if (workId != null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "date", dateWork);
            addTeg(stringBuilder, "projectId", projectId);
            String uri = "/refresh/" + workId + stringBuilder;
            return webClient.get().uri("/refresh/" + workId + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, uri + " ЗИ c id = " + workId + " не найдена"))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(uri, throwable))
                    .block();
        }
        return false;
    }

    public Boolean getRate(List<Long> workIdList, Long projectId) {
        Boolean flag = false;
        if (workIdList != null && !workIdList.isEmpty() && projectId != null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "projectId", projectId);
            addTeg(stringBuilder, "workId", workIdList);
            String uri = "/rate" + stringBuilder;
            flag = webClient.get().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "ЗИ c id = " + workIdList + " не найдена"))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(uri, throwable))
                    .block();
        }
        return flag;
    }

    public void addProject(Long workId, Long projectId) {
        if (workId != null && projectId != null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "projectId", projectId);
            String uri = "/project/add/" + workId + stringBuilder;
            webClient.get().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "ЗИ c id = " + workId + " не найдена"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(uri, throwable))
                    .block();
        }

    }

    public List<WorkRepDto> getTimeWork(String ziName,
                                        Boolean availWork,
                                        Integer stageZi,
                                        Long releaseId,
                                        Long projectId,
                                        LinkedList<String> sort,
                                        Boolean addMedium
    ) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "ziName", ziName);
        addTeg(stringBuilder, "availWork", availWork);
        addTeg(stringBuilder, "stageZi", stageZi);
        addTeg(stringBuilder, "releaseId", releaseId);
        addTeg(stringBuilder, "projectId", projectId);
        addTeg(stringBuilder, "addMedium", addMedium);


        addTeg(stringBuilder, "sort", sort);
        String uri = "/rep" + stringBuilder;
        try {
            return webClient.get().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по ЗИ"))
                    .bodyToFlux(WorkRepDto.class)
                    .collectList()
                    .doOnError(throwable -> log.error(uri, throwable))
                    .block();
        } catch (RuntimeException ex) {
            log.error(uri, ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public List<WorkUserTime> getWorkUserTime(boolean ziSplit, Long projectId, Timestamp dateStart, Timestamp dateEnd) {
        return getWorkUserTime(ziSplit,
                null,
                true,
                false,
                dateStart,
                dateEnd,
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

                                              List<String> sort) {
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
        String uri = "/rep/fact/week" + stringBuilder;
        try {
            return webClient.get().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные по ЗИ"))
                    .bodyToFlux(WorkUserTime.class)
                    .collectList()
                    .doOnError(throwable -> log.error(uri, throwable))
                    .block();
        } catch (RuntimeException ex) {
            log.error(uri, ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

}
