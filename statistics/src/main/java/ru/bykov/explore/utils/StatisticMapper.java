package ru.bykov.explore.utils;

import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.dto.StatisticDto;

public class StatisticMapper {

    public static Statistic toStatistic(StatisticDto statisticDto){
        return Statistic.builder().build();
    }

    public static StatisticDto toStatisticDto(Statistic statistic){
        return StatisticDto.builder().build();
    }

}
