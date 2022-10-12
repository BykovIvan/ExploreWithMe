package ru.bykov.explore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.dto.EndPointHit;
import ru.bykov.explore.model.dto.ViewStats;
import ru.bykov.explore.repository.StatisticRepository;
import ru.bykov.explore.service.StatisticService;
import ru.bykov.explore.utils.StatisticMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatisticRepository statisticRepository;

    @Override
    @Transactional
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
            return statisticRepository.findByParamByUniqueIp(timeOfStart, timeOfEnd, uris);
        }
        return statisticRepository.findByParam(timeOfStart, timeOfEnd, uris);
    }

}
