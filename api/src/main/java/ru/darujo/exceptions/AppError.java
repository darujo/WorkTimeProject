package ru.darujo.exceptions;

import java.io.Serializable;

public class AppError implements Serializable {
    public AppError() {
    }

    private Integer statusCode;
    private String message;

    public AppError(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
