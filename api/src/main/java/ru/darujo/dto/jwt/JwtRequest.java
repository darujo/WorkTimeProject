package ru.darujo.dto.jwt;

import java.io.Serializable;

public class JwtRequest implements Serializable {
    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public JwtRequest() {
    }
}

