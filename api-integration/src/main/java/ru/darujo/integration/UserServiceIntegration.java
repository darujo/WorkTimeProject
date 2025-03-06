package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.UserDto;
import ru.darujo.exceptions.ResourceNotFoundException;

@Component
public class UserServiceIntegration {
    private WebClient webClientUser;

    @Autowired
    public void setWebClientUser(WebClient webClientUser) {
        this.webClientUser = webClientUser;
    }

    public UserDto getUserDto(Long userId, String nikName) {
        StringBuilder stringBuilder = new StringBuilder();
        if(userId != null){
            stringBuilder.append("/").append(userId);
        }
        else{
            if (nikName != null) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("&");
                } else{
                    stringBuilder.append("?");
                }
                stringBuilder.append("nikName=").append(nikName);
            }
        }

        return webClientUser.get().uri(stringBuilder.toString())
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные по затраченому времени")))
                .bodyToMono(UserDto.class)
                .block();
    }
}
