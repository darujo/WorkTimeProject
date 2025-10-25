package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.exceptions.ResourceNotFoundRunTime;


@Component
public class TelegramServiceIntegration extends ServiceIntegration {
    private WebClient webClientTelegram;

    @Autowired
    public void setWebClientTelegram(WebClient webClientTelegram) {
        this.webClientTelegram = webClientTelegram;
    }

    public void sendMessage(
            String author,
            String chatId,
            String text) {
        try {
            webClientTelegram.post().uri("/" + chatId + "/notifications")
                    .header("username",author)
                    .bodyValue(text)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить ответ от сервиса telegram")))
                    .bodyToMono(Void.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-WorkTime) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
}
