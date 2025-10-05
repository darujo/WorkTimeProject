package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

@Component
@ConditionalOnMissingClass
public class RateServiceIntegration extends ServiceIntegration {
    private WebClient webClientRate;

    @Autowired
    public void setWebClientCalendar(WebClient webClientRate) {
        this.webClientRate = webClientRate;
    }


    public WorkStageDto getTimePlan(Long workId) {
        try {
            if (workId == null) {
                throw new ResourceNotFoundRunTime("Не задана workId для получения плановых затрат");
            }
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "workId", workId);
            return webClientRate.get().uri("/time/all" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить плановые трудозатраты")))
                    .bodyToMono(WorkStageDto.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить План " + ex.getMessage());
        }
    }

}
