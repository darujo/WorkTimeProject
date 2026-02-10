package ru.darujo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

@Slf4j
@Component
@ConditionalOnMissingClass
public class RateServiceIntegration extends ServiceIntegration {
    @Autowired
    public void setWebClient(WebClient webClientRate) {
        super.setWebClient(webClientRate);
    }


    public WorkStageDto getTimePlan(Long workId, Long projectId) {
        try {
            if (workId == null) {
                throw new ResourceNotFoundRunTime("Не задана workId для получения плановых затрат");
            }
            StringBuilder stringBuilder = new StringBuilder();
            addTeg(stringBuilder, "workId", workId);
            addTeg(stringBuilder, "projectId", projectId);
            log.error(stringBuilder.toString());
            return webClient.get().uri("/time/all" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить плановые трудозатраты"))
                    .bodyToMono(WorkStageDto.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {

            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить План " + ex.getMessage());
        }
    }

}
