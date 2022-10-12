package ru.bykov.explore.utils;

import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.dto.EndPointHit;
import ru.bykov.explore.model.dto.ViewStats;

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

    public static ViewStats toViewStats(Statistic statistic, Long hits) {
        return ViewStats.builder()
                .app(statistic.getApp())
                .uri(statistic.getUri())
                .hits(hits)
                .build();
    }

}
