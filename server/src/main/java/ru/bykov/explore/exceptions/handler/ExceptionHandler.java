package ru.bykov.explore.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bykov.explore.exceptions.NoParamInRequestException;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.exceptions.model.ApiError;

@RestControllerAdvice
public class ExceptionHandler {

    /**
     * Все ситуаций, когда искомый объект не найден, код 404
     * All situations when the desired object is not found
     */
//    @org.springframework.web.bind.annotation.ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ApiError handleIncorrectParameterException(final NotFoundException e) {
//        return new ApiError(e.getMessage());
//    }
//
//    @org.springframework.web.bind.annotation.ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiError handleIncorrectParameterException(final NoParamInRequestException e) {
//        return new ApiError(e.getMessage());
//    }
}
