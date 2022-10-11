package ru.bykov.explore.service;

import ru.bykov.explore.model.dto.EndPointHit;
import ru.bykov.explore.model.dto.ViewStats;

import java.util.List;

public interface StatisticService {

    void createStat(EndPointHit endPointHit);

    List<ViewStats> getStatsByParam(String start, String end, String[] uris, Boolean unique);
}
