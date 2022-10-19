package ru.bykov.explore.utils;

import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.dto.EndPointHit;

import java.time.LocalDateTime;

public class StatisticMapper {

    public static Statistic toStatistic(EndPointHit endPointHit, LocalDateTime timeOfGetEvents) {
        return Statistic.builder()
                .app(endPointHit.getApp())
                .uri(endPointHit.getUri())
                .ip(endPointHit.getIp())
                .timestamp(timeOfGetEvents)
                .build();
    }
}
