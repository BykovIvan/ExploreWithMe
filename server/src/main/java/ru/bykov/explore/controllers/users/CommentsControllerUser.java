package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;
import ru.bykov.explore.services.CommentService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class CommentsControllerUser {

    private final CommentService commentService;

    //Добавление комментария пользователем событию
    @PostMapping("{ownerId}/events/{eventId}/comments")
    public CommentDto addCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                         @PathVariable("eventId") Long eventId,
                                         @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment добавление " +
                "комментария пользователем id = {} к событию id = {}. Метод POST.", ownerId, eventId);
        return commentService.addCommentToEventFromUser(ownerId, eventId, newCommentDto);
    }

    //Получение у события своего комментария пользователем
    @GetMapping("{ownerId}/events/{eventId}/comments/{comId}")
    public CommentDto addCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                         @PathVariable("eventId") Long eventId,
                                         @PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment получение " +
                "комментария id = {} пользователем id = {} к событию id = {}. Метод GET.", comId, ownerId, eventId);
        return commentService.findCommentByIdFromUser(ownerId, eventId, comId);
    }

    //Удаление у события своего комментария пользователем
    @DeleteMapping("{ownerId}/events/{eventId}/comments/{comId}")
    public CommentDto addCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                         @PathVariable("eventId") Long eventId,
                                         @PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment удаление " +
                "комментария id = {} пользователем id = {} к событию id = {}. Метод GET.", comId, ownerId, eventId);
        return commentService.deleteCommentByIdFromUser(ownerId, eventId, comId);
    }

    //Получение у события своего комментария пользователем
    @GetMapping("{ownerId}/events/{eventId}/comments/search")
    public List<CommentDto> addCommentFromUser(@PathVariable("ownerId") Long ownerId,
                                               @PathVariable("eventId") Long eventId,
                                               @RequestParam(value = "text", required = false) String text) {
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment поиск " +
                "комментария пользователем id = {} к событию id = {}. Метод GET.", ownerId, eventId);
        return commentService.searchCommentsByParamFromUser(ownerId, eventId, text);
    }


}
