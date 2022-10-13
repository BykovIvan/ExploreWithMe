package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;
import ru.bykov.explore.services.CompilationService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "admin/compilations")
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен запрос к эндпоинту /admin/compilations метод POST");
        return compilationService.createFromAdmin(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable("compId") Long compId) {
        log.info("Получен запрос к эндпоинту /admin/compilations метод Delete по id = {}", compId);
        compilationService.deleteByIdFromAdmin(compId);
    }

    //удаление события из подборки
    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromComp(@PathVariable("compId") Long compId,
                                    @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /admin/compilations/{compId}/events/{eventId} метод Delete, удаление события id = {} из подборки id = {}", eventId, compId);
        compilationService.deleteEventFromCompFromAdmin(compId, eventId);
    }

    //добавление события в подборку
    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable("compId") Long compId,
                                      @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /admin/compilations/{compId}/events/{eventId} метод PATCH, добавление события id = {} к подборке id = {}", eventId, compId);
        compilationService.addEventToCompFromAdmin(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deleteCompFromMainPage(@PathVariable("compId") Long compId) {
        log.info("Получен запрос к эндпоинту /admin/compilations/{compId}/pin метод DELETE, удаление подборки id = {} с главной страницы", compId);
        compilationService.deleteCompFromMainPageFromAdmin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void addCompFromMainPage(@PathVariable("compId") Long compId) {
        log.info("Получен запрос к эндпоинту /admin/compilations/{compId}/pin метод PATCH, закрепление подборки id = {} на главную страницу", compId);
        compilationService.addCompFromMainPageFromAdmin(compId);
    }


}
