package ru.bykov.explore.exceptions;

public class NoParamInRequestException extends RuntimeException {
    public NoParamInRequestException(String message) {
        super(message);
    }
}