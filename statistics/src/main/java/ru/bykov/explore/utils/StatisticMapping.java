package ru.bykov.explore.utils;

import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.StatisticDto;

public class StatisticMapping {

    public static Statistic toStatistic(StatisticDto statisticDto){
        return Statistic.builder().build();
    }

    public static StatisticDto toStatisticDto(Statistic statistic){
        return StatisticDto.builder().build();
    }

}
