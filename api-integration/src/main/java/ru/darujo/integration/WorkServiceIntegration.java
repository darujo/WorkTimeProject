package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.WorkLittleDto;
import ru.darujo.exceptions.ResourceNotFoundException;


@Component
public class WorkServiceIntegration {
    private WebClient webClientWork;

    @Autowired
    public void setWebClientWork(WebClient webClientWork) {
        this.webClientWork = webClientWork;
    }

    public WorkLittleDto getWorEditDto(Long workId) {
        if (workId == null){
            return new WorkLittleDto();
        }

        return webClientWork.get().uri("/obj/little/" + workId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
                        ,
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по ЗИ с ID = " + workId)))
                .bodyToMono(WorkLittleDto.class)
                .block();
    }
}
