package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;

import java.util.List;
import java.util.Optional;

public interface CompilationService {

    /**
     * Получение подборок событий
     * Getting compilations of events
     */
    List<CompilationDto> findAllForAllByParam(Boolean pinned, Integer from, Integer size);

    /**
     * Получение подборки событий по его id
     * Getting a compilation of events by its id
     */
    CompilationDto findByIdForAll(Long compilationId);

    /**
     * Добавление новой подборки администратором
     * Adding a new compilation by administrator
     */
    CompilationDto createFromAdmin(NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки администратором
     * Removing a compilation by an administrator
     */
    void deleteByIdFromAdmin(Long compId);

    /**
     * Удаление события из подборки администратором
     * Removing an event from a compilation by an administrator
     */
    void deleteEventFromCompFromAdmin(Long compId, Long eventId);

    /**
     * Добавление события в подборку администратором
     * Adding an event to a compilation by an administrator
     */
    void addEventToCompFromAdmin(Long compId, Long eventId);

    /**
     * Открепить подборку на главной странице администратором
     * Unpin a compilation on the main page by the administrator
     */
    void deleteCompFromMainPageFromAdmin(Long compId);

    /**
     * Закрепить подборку на главной странице администратором
     * Pin a compilation on the main page by the administrator
     */
    void addCompFromMainPageFromAdmin(Long compId);

}
