package ru.bykov.explore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.StatisticDto;
import ru.bykov.explore.repository.StatisticRepository;
import ru.bykov.explore.service.StatisticService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public Statistic createStat(Statistic statistic) {
        return statisticRepository.save(statistic);
    }

    @Override
    public List<StatisticDto> getStatsByParam(String start, String end, String[] uris, Boolean unique) {
        return null;
    }
}
