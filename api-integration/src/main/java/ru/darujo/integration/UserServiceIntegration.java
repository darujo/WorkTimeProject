package ru.darujo.integration;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.CustomPageImpl;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserFio;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class UserServiceIntegration extends ServiceIntegration {
    private WebClient webClientUser;

    @Autowired
    public void setWebClientUser(WebClient webClientUser) {
        this.webClientUser = webClientUser;
    }
    private static UserServiceIntegration INSTANCE;
    public static UserServiceIntegration getInstance(){
        return INSTANCE;
    }
    @PostConstruct
    public void init(){
        INSTANCE = this;
    }
    public UserDto getUserDto(Long userId, String nikName) {
        StringBuilder stringBuilder = new StringBuilder();
        if (userId != null) {
            stringBuilder.append("/").append(userId);
        } else {
            addTeg(stringBuilder, "nikName", nikName);
        }
        try {
            return webClientUser.get().uri("/user" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные пользователю"))
                    .bodyToMono(UserDto.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundRunTime) {
                throw ex;
            } else {
                throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }
    }

    public List<UserDto> getUserDTOs(String nikNameOrRole) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "nikName", nikNameOrRole);
        try {
            return Objects.requireNonNull(webClientUser.get().uri(stringBuilder.toString())
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> getMessage(clientResponse, "Что-то пошло не так не удалось получить данные пользователю"))
                    .bodyToMono(new ParameterizedTypeReference<@org.jspecify.annotations.NonNull CustomPageImpl<UserDto>>() {
                    })
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block()).getContent();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundRunTime) {
                throw ex;
            } else {
                throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }

    }

    public ResultMes linkCodeTelegram(Integer code, Long telegramId, Integer threadId) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "code", code);
        addTeg(stringBuilder, "telegramId", telegramId);
        addTeg(stringBuilder, "threadId", threadId);
        try {
            return webClientUser.get().uri("/user/telegram/link" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> getMessage(clientResponse, "Что-то пошло не так не удалось получить данные пользователю"))
                    .bodyToMono(ResultMes.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundRunTime) {
                throw ex;
            } else {
                throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }

    }

    public Boolean linkDeleteTelegram(Long telegramId, Integer threadId) {
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder, "telegramId", telegramId);
        // todo добавит и проверить что на другой стороне
        try {
            return webClientUser.get().uri("/user/telegram/delete" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> getMessage(clientResponse, "Что-то пошло не так не удалось получить данные пользователю"))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundRunTime) {
                throw ex;
            } else {
                throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }

    }

    public MapUserInfoDto getUserMessageDTOs() {
        try {
            return webClientUser.get().uri("/information")
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> getMessage(clientResponse, "Что-то пошло не так не удалось получить данные пользователю"))
                    .bodyToMono(MapUserInfoDto.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundRunTime) {
                throw ex;
            } else {
                throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }
    }

    public ResultMes checkUserTelegram(@NonNull Long chatId) {
        try {
            return webClientUser.get().uri("/user/telegram/get/" + chatId)
                    .retrieve()
//                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
//                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить данные пользователю" + clientResponse.bodyToMono(String.class).flatMap(s -> {log.error(s);
//                                return Mono.just(s);}))))
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> getMessage(clientResponse, "Что-то пошло не так не удалось получить данные пользователю :")
                    )
                    .bodyToMono(ResultMes.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            if (ex instanceof ResourceNotFoundRunTime) {
                throw ex;
            } else {
                throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить пользователя (api-auth) не доступен подождите или обратитесь к администратору " + ex.getMessage());
            }
        }
    }
    private final Map<String, UserDto> userDtoMap = new HashMap<>();
    public void updFio(UserFio userFio) {
        try {
            if (userFio.getNikName() != null) {
                UserDto userDto = userDtoMap.get(userFio.getNikName());
                if (userDto == null) {
                    userDto = getUserDto(null, userFio.getNikName());
                    userDtoMap.put(userFio.getNikName(), userDto);
                }
                userFio.setFirstName(userDto.getFirstName());
                userFio.setLastName(userDto.getLastName());
                userFio.setPatronymic(userDto.getPatronymic());
            }
        } catch (ResourceNotFoundRunTime e) {
            log.error(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }
}
