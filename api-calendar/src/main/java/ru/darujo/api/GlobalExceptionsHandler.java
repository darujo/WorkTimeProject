package ru.darujo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.darujo.exceptions.AppError;
import ru.darujo.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> catchResourceNotFoundException(ResourceNotFoundException e){
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value() ,e.getMessage()),HttpStatus.NOT_FOUND);
    }

}
