package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.CompilationDto;
import ru.bykov.explore.services.CompilationService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "admin/compilations")
public class CompilationControllerAdmin {

    private final CompilationService compilationService;


    //TODO Должен уходить ответ 201
    @PostMapping
    public CompilationDto create(@RequestBody CompilationDto compilationDto){
        log.info("Получен запрос к эндпоинту /admin/compilations метод POST");
        return compilationService.createByAdmin(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable("compId") Long compId){
        log.info("Получен запрос к эндпоинту /admin/compilations метод Delete по id = {}", compId);
        compilationService.deleteByIdByAdmin(compId);
    }

    //удаление события из подборки
    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompByAdmin(@PathVariable("compId") Long compId,
                                           @PathVariable("eventId") Long eventId){
        log.info("Получен запрос к эндпоинту /admin/compilations/{compId}/events/{eventId} метод Delete, удаление события id = {} из подборки id = {}", eventId , compId);
        compilationService.deleteEventFromCompByAdmin(compId, eventId);
    }

}
