package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.WorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;


@Component
public class WorkServiceIntegration {
    private WebClient webClientWork;

    @Autowired
    public void setWebClientWork(WebClient webClientWork) {
        this.webClientWork = webClientWork;
    }

    public WorkDto getWork(Long id) {
        return webClientWork.get().uri("/" + id)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Задача c id = " + id + " не найдена")))
                .bodyToMono(WorkDto.class)
                .block();
    }
}
