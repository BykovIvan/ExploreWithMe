package ru.bykov.explore.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {
    private Long hits;      //Количество просмотров
    private String app;     //название сервиса
    private String uri;     //URI сервиса

}
