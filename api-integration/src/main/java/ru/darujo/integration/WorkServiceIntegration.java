package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.util.Date;


@Component
public class WorkServiceIntegration extends ServiceIntegration{
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
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить данные по ЗИ с ID = " + workId)))
                    .bodyToMono(WorkLittleDto.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public MapStringFloat getWorkTimeStageFact(Long workId){
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"workId",workId);

        try {
            return webClientWork.get().uri("/rep/time/fact/stage0" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                            ,
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить данные по ЗИ с ID = " + workId)))
                    .bodyToMono(MapStringFloat.class)
                    .block();
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить ЗИ (api-work) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Boolean setWorkDate(Long workId, Date dateWork) {
        if(workId!= null) {
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "date", dateWork);
            return webClientWork.get().uri("/refresh/" + workId + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("ЗИ c id = " + workId + " не найдена")))
                    .bodyToMono(Boolean.class)
                    .block();
        }
        return false;
    }

}
