package ru.darujo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.darujo.exceptions.AppError;

import java.net.UnknownHostException;

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> catchUnknownHostException(UnknownHostException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }


}
