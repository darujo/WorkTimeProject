package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.UserDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.List;

@Component
public class UserServiceIntegration {
    private WebClient webClientUser;

    @Autowired
    public void setWebClientUser(WebClient webClientUser) {
        this.webClientUser = webClientUser;
    }

    public UserDto getUserDto(Long userId, String nikName) {
        StringBuilder stringBuilder = new StringBuilder();
        if (userId != null) {
            stringBuilder.append("/").append(userId);
        } else {
            if (nikName != null) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("&");
                } else {
                    stringBuilder.append("?");
                }
                stringBuilder.append("nikName=").append(nikName);
            }
        }
        try {
            return webClientUser.get().uri("/user" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные пользователю")))
                    .bodyToMono(UserDto.class)
                    .block();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundException)
            {
              throw ex;
            }
            else {
                throw new ResourceNotFoundException("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }
    }
    public List<UserDto> getUserDTOs(String role) {
        StringBuilder stringBuilder = new StringBuilder();
        if (role != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append("role=").append(role);
        }
        try {
            return webClientUser.get().uri( stringBuilder.toString())
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные пользователю")))
                    .bodyToFlux(UserDto.class)
                    .collectList()
                    .block();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundException)
            {
                throw ex;
            }
            else {
                throw new ResourceNotFoundException("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }
    }
}
