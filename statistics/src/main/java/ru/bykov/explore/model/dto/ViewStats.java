package ru.bykov.explore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {
    private String app;     //название сервиса
    private String uri;     //URI сервиса
    private Long hits;      //Количество просмотров
}
