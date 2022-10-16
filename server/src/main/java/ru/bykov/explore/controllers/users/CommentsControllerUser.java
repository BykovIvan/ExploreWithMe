package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.CommentShortDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;
import ru.bykov.explore.model.dto.comment.UpdateCommentDto;
import ru.bykov.explore.services.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class CommentsControllerUser {

    private final CommentService commentService;

    @PostMapping("{ownerId}/events/{eventId}/comments")
    public CommentDto addCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                         @PathVariable("eventId") Long eventId,
                                         @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment добавление " +
                "комментария пользователем id = {} к событию id = {}. Метод POST.", ownerId, eventId);
        return commentService.addCommentToEventFromUser(ownerId, eventId, newCommentDto);
    }

    @PatchMapping("{ownerId}/events/{eventId}/comments/{comId}")
    public CommentDto updateCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                            @PathVariable("eventId") Long eventId,
                                            @PathVariable("comId") Long comId,
                                            @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment добавление " +
                "комментария id = {} пользователем id = {} к событию id = {}. Метод POST.", comId, ownerId, eventId);
        return commentService.updateCommentToEventFromUser(ownerId, eventId, comId, updateCommentDto);
    }

    @GetMapping("{ownerId}/events/{eventId}/comments/{comId}")
    public CommentDto findCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                          @PathVariable("eventId") Long eventId,
                                          @PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment получение " +
                "комментария id = {} пользователем id = {} к событию id = {}. Метод GET.", comId, ownerId, eventId);
        return commentService.findCommentByIdFromUser(ownerId, eventId, comId);
    }

    @DeleteMapping("{ownerId}/events/{eventId}/comments/{comId}")
    public void deleteCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                      @PathVariable("eventId") Long eventId,
                                      @PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment удаление " +
                "комментария id = {} пользователем id = {} к событию id = {}. Метод GET.", comId, ownerId, eventId);
        commentService.deleteCommentByIdFromUser(ownerId, eventId, comId);
    }

    //
    @GetMapping("{ownerId}/events/{eventId}/comments/search")
    public List<CommentShortDto> searchCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                                       @PathVariable("eventId") Long eventId,
                                                       @RequestParam(value = "text") String text,
                                                       @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment поиск " +
                "комментария пользователем id = {} к событию id = {} текст = {}. Метод GET.", ownerId, eventId, text);
        return commentService.searchCommentsByParamFromUser(ownerId, eventId, text, from, size);
    }


}
