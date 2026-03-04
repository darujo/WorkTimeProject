package ru.darujo.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.darujo.exceptions.AppError;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    AppError catchResourceNotFoundRunTime(ResourceNotFoundRunTime e) {

        return new AppError(HttpStatus.NOT_FOUND.value() ,e.getMessage());
    }

}
