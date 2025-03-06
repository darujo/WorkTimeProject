package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.UserDto;

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
                .bodyToMono(UserDto.class)
                .block();
    }
}
