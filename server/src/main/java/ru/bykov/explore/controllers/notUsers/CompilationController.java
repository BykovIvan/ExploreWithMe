package ru.bykov.explore.controllers.notUsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.services.CompilationService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> allCompilationsByParam(@RequestParam(value = "pinned") Boolean pinned,
                                                       @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту /compilations получение всех по параметрам. Метод GET");
        return compilationService.findAllForAllByParam(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto compilationById(@PathVariable("compilationId") Long compilationId) {
        log.info("Получен запрос к эндпоинту /compilations получение по id. Метод GET");
        return compilationService.findByIdForAll(compilationId);
    }

}
