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
import ru.darujo.dto.jwt.JwtRequest;
import ru.darujo.dto.jwt.JwtResponse;
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
    @Autowired
    public void setWebClient(WebClient webClientUser) {
        super.setWebClient(webClientUser);
    }
    private static UserServiceIntegration INSTANCE;
    public static UserServiceIntegration getInstance(){
        return INSTANCE;
    }
    @PostConstruct
    public void init(){
        INSTANCE = this;
    }

    public JwtResponse getToken(String username, String password) {

        try {
            return webClient.post().uri("/auth/")
                    .bodyValue(new JwtRequest(username, password))
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, "Что-то пошло не так не удалось получить данные пользователю"))
                    .bodyToMono(JwtResponse.class)
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

    public UserDto getUserDto(Long userId, String nikName) {
        StringBuilder stringBuilder = new StringBuilder();
        if (userId != null) {
            stringBuilder.append("/").append(userId);
        } else {
            addTeg(stringBuilder, "nikName", nikName);
        }
        try {
            return webClient.get().uri("/users/user" + stringBuilder)
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
            return Objects.requireNonNull(webClient.get().uri("/users" + stringBuilder)
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
            return webClient.get().uri("/users/user/telegram/link" + stringBuilder)
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
        addTeg(stringBuilder, "threadId", threadId);
        try {
            return webClient.get().uri("/users/user/telegram/delete" + stringBuilder)
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
            return webClient.get().uri("/users/information")
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
            return webClient.get().uri("/users/user/telegram/get/" + chatId)
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

    public String getFio(String nikName) {
        try {
            if (nikName != null) {
                UserDto userDto = getUserDto(nikName);
                return userDto.getLastName() + " " + userDto.getFirstName() + " " + userDto.getPatronymic();
            }
        } catch (ResourceNotFoundRunTime e) {
            log.error(e.getMessage());
            return "Не найден пользователь с ником " + nikName;
        }
        return "";
    }
    public void updFio(UserFio userFio) {
        try {
            if (userFio.getNikName() != null) {
                UserDto userDto = getUserDto(userFio.getNikName());
                userFio.setFirstName(userDto.getFirstName());
                userFio.setLastName(userDto.getLastName());
                userFio.setPatronymic(userDto.getPatronymic());
            }
        } catch (ResourceNotFoundRunTime e) {
            log.error(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }

    private UserDto getUserDto(String nikName) {
        UserDto userDto = userDtoMap.get(nikName);
        if (userDto == null) {
            userDto = getUserDto(null, nikName);
            userDtoMap.put(nikName, userDto);
        }
        return userDto;
    }
}
