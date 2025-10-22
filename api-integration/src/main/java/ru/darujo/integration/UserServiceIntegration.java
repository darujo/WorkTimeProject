package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.CustomPageImpl;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.dto.user.UserDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Objects;


@Component
public class UserServiceIntegration extends ServiceIntegration {
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
            addTeg(stringBuilder,"nikName",nikName);
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
        addTeg(stringBuilder,"nikName",nikNameOrRole);
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
    public ResultMes linkCodeTelegram(Integer code, Long telegramId) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"code",code);
        addTeg(stringBuilder,"telegramId",telegramId);
        try {
            return webClientUser.get().uri("/user/telegram/link" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные пользователю")))
                    .bodyToMono(ResultMes.class)
                    .block();
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
    public Boolean linkDeleteTelegram(Long telegramId) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"telegramId",telegramId);
        try {
            return webClientUser.get().uri("/user/telegram/delete" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные пользователю")))
                    .bodyToMono(Boolean.class)
                    .block();
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

    public MapUserInfoDto getUserMessageDTOs() {
        try {
            return webClientUser.get().uri("/information" )
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить данные пользователю")))
                    .bodyToMono(MapUserInfoDto.class)
                    .block();
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
