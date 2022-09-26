package ru.bykov.explore.service;

import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.StatisticDto;

import java.util.List;

public interface StatisticService {
    Statistic createStat(Statistic statistic);

    List<StatisticDto> getStatsByParam(String start, String end, String[] uris, Boolean unique);
}
