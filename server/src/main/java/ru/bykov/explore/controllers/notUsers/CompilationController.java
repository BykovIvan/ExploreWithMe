package ru.bykov.explore.controllers.notUsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bykov.explore.model.dto.NewCompilationDto;
import ru.bykov.explore.services.CompilationService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<NewCompilationDto> allCompilations() {
        log.info("Получен запрос к эндпоинту /compilations получение всех. Метод GET");
        return compilationService.getAllForAll();
    }

    @GetMapping("/{compilationId}")
    public NewCompilationDto compilationById(@PathVariable("compilationId") Long compilationId) {
        log.info("Получен запрос к эндпоинту /compilations получение по id. Метод GET");
        return compilationService.getByIdForAll(compilationId);
    }

}
