package ru.darujo.dto.jwt;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private String token;

    public String getToken() {
        return token;
    }

    public JwtResponse(String token) {
        this.token = token;
    }
}
