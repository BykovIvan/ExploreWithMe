package ru.bykov.explore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.dto.EndPointHit;
import ru.bykov.explore.model.dto.ViewStats;
import ru.bykov.explore.repository.StatisticRepository;
import ru.bykov.explore.service.StatisticService;
import ru.bykov.explore.utils.StatisticMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatisticRepository statisticRepository;

    @Override
    public void createStat(EndPointHit endPointHit) {
        LocalDateTime timeOfGetEvents = LocalDateTime.now();
        Statistic statistic = StatisticMapper.toStatistic(endPointHit, timeOfGetEvents);
        statisticRepository.save(statistic);
    }

    @Override
    public List<ViewStats> getStatsByParam(String start, String end, String[] uris, Boolean unique) {
        LocalDateTime timeOfStart = LocalDateTime.parse(start, formatter);
        LocalDateTime timeOfEnd = LocalDateTime.parse(end, formatter);
        if (unique) {
            return statisticRepository.getByParam(timeOfStart, timeOfEnd, uris)
                    .stream()
                    .map((Statistic statistic) -> StatisticMapper.toViewStats(statistic, statisticRepository.countByAppAndUri(statistic.getApp(), statistic.getUri())))
                    .collect(Collectors.toList());
        }
        return statisticRepository.getByParamUniqIp(timeOfStart, timeOfEnd, uris)
                .stream()
                .map((Statistic statistic) -> StatisticMapper.toViewStats(statistic, statisticRepository.countByAppAndUri(statistic.getApp(), statistic.getUri())))
                .collect(Collectors.toList());
    }

    @Override
    public Long getCountOfEvent(String app, String uri) {
        return statisticRepository.countByAppAndUri(app, uri);
    }
}
