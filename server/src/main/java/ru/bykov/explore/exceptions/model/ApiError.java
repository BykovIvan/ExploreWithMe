package ru.bykov.explore.exceptions.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс для формирования ответа при ошибках
 * Class for generating a response in case of errors
 */

@Data
@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private List<String> errors; //Список стектрейсов или описания ошибок
    private String message;     //Сообщение об ошибке
    private String reason;      //Общее описание причины ошибки
    private HttpStatus status;      //Код статуса HTTP-ответа из енум
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;    //Дата и время когда произошла ошибка

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.reason = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.reason = ex.getLocalizedMessage();
    }

    private ApiError() {
        timestamp = LocalDateTime.now();
    }
}