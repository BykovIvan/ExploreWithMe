package ru.bykov.explore.service;

import ru.bykov.explore.model.dto.EndPointHit;
import ru.bykov.explore.model.dto.ViewStats;

import java.util.List;

public interface StatisticService {

    /**
     * Сохранение информации о том, что к эндпоинту был запрос.
     * Saving information that there was a request to the endpoint.
     */
    void createStat(EndPointHit endPointHit);

    /**
     * Получение статистики по посещениям. Дата закодирована.
     * Getting statistics on visits. The date is encoded.
     */
    List<ViewStats> getStatsByParam(String start, String end, String[] uris, Boolean unique);
}
