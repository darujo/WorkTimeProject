package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.CustomPageImpl;
import ru.darujo.dto.user.UserDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Objects;


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
    public List<UserDto> getUserDTOs(String nikNameOrRole) {
        StringBuilder stringBuilder = new StringBuilder();
        if (nikNameOrRole != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }
            stringBuilder.append("nikName=").append(nikNameOrRole);
        }
        try {
            return Objects.requireNonNull(webClientUser.get().uri(stringBuilder.toString())
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные пользователю")))
                    .bodyToMono(new ParameterizedTypeReference<CustomPageImpl<UserDto>>() {
                    })
                    .block()).getContent();
        }
        catch (RuntimeException ex) {
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
