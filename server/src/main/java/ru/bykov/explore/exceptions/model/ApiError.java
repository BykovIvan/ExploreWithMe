package ru.bykov.explore.exceptions.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Класс для формирования ответа при ошибках
 * Class for generating a response in case of errors
 */

public class ApiError {
    private final String error;                 //Название ошибки

    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

    public ApiError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}