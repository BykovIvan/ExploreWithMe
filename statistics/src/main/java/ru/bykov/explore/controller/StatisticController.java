package ru.bykov.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.EndPointHit;
import ru.bykov.explore.model.dto.ViewStats;
import ru.bykov.explore.service.StatisticService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping()
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public void createStat(@RequestBody EndPointHit endPointHit) {
        log.info("Получен запрос к эндпоинту /hit, сохранение статистики");
        statisticService.createStat(endPointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStatByParam(@RequestParam(value = "start") String start,
                                          @RequestParam(value = "end") String end,
                                          @RequestParam(value = "uris", required = false) String[] uris,
                                          @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Получен запрос к эндпоинту /stats, получение статистики по параметрам");
        return statisticService.getStatsByParam(start, end, uris, unique);
    }
}
