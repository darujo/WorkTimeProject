package ru.darujo.integration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public abstract class ServiceIntegration {
    @Data
    protected static class ErrorResponse {
        private String message;
        private int errorCode;
    }

    protected Mono<? extends @NonNull Throwable> getMessage(ClientResponse clientResponse, String message) {
        return clientResponse
                .bodyToMono(ErrorResponse.class)
                .flatMap(error ->{
                            log.error("{} {}", message, error.getMessage());
                        return Mono.error(new ResourceNotFoundRunTime(message + " " + error.getMessage()));
                }

                );
    }

    protected void addTeg(StringBuilder stringBuilder, String str, Enum<?> value) {
        addTeg(stringBuilder, str, value.toString());
    }

    protected void addTeg(StringBuilder stringBuilder, String str, Date value) {
        addTeg(stringBuilder, str, dateToText(value));
    }

    protected void addTeg(StringBuilder stringBuilder, String str, String value) {
        if (value != null && !value.isEmpty()) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append(str).append("=").append(value);
        }
    }

    protected void addTeg(StringBuilder stringBuilder, String str, Boolean value) {
        if (value != null) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append(str).append("=").append(value);
        }
    }

    protected void addTeg(StringBuilder stringBuilder, String str, Long value) {
        if (value != null) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append(str).append("=").append(value);
        }
    }

    protected void addTeg(StringBuilder stringBuilder, String str, Integer value) {
        if (value != null) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append(str).append("=").append(value);
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date) + "T00:00:00.000Z";
    }

}
