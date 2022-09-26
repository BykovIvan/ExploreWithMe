package ru.bykov.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.StatisticDto;
import ru.bykov.explore.service.StatisticService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping()
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public Statistic createStat(@RequestBody Statistic statistic){
        log.info("Получен запрос к эндпоинту /hit, сохранение статистики");
        return statisticService.createStat(statistic);
    }

    @GetMapping("/stats")
    public List<StatisticDto> getStatByParam(@RequestParam(value = "start", required = false) String start,
                                             @RequestParam(value = "end", required = false) String end,
                                             @RequestParam(value = "uris", required = false) String[] uris,
                                             @RequestParam(value = "unique", required = false) Boolean unique){
        log.info("Получен запрос к эндпоинту /stats, получение статистики по параметрам");
        return statisticService.getStatsByParam(start, end, uris, unique);
    }



}
