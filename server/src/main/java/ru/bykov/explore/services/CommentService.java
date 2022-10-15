package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;

public interface CommentService {
    /**
     * Добавление комментария к событию текущим пользователем.
     * Adding a comment to the event by the current user.
     */
    CommentDto addCommentToEventFromUser(Long userId, Long eventId, NewCommentDto newCommentDto);
}
