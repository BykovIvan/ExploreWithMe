package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;
import ru.bykov.explore.services.CommentService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class CommentsControllerUser {

    private final CommentService commentService;

    @PostMapping("{userID}/event/{eventId}/comment")
    public CommentDto addCommentFromUser(@PathVariable("userId") Long userId,
                                         @PathVariable("eventId") Long eventId,
                                         @RequestBody NewCommentDto newCommentDto){
        log.info("Получен запрос к эндпоинту /users/{userID}/{eventId}/comment добавление " +
                "комментария пользователем id = {} к событию id = {}. Метод POST.", userId, eventId);
        return commentService.addCommentToEventFromUser(userId, eventId, newCommentDto);
    }


}
