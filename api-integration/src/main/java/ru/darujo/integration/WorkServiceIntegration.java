package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.WorkEditDto;


@Component
public class WorkServiceIntegration {
    private WebClient webClientWork;

    @Autowired
    public void setWebClientWork(WebClient webClientWork) {
        this.webClientWork = webClientWork;
    }

    public WorkEditDto getWorEditDto(Long workId) {
        return webClientWork.get().uri("/" + workId)
                .retrieve()
//                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value()
//                        ,
//                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                .bodyToMono(WorkEditDto.class)
                .block();
    }
}
