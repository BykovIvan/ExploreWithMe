package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;
import ru.bykov.explore.model.dto.comment.UpdateCommentByAdmin;
import ru.bykov.explore.model.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    /**
     * Добавление комментария к событию текущим пользователем.
     * Adding a comment to the event by the current user.
     */
    CommentDto addCommentToEventFromUser(Long userId, Long eventId, NewCommentDto newCommentDto);

    /**
     * Обновление комментария пользователем, может менять текст и менять статут на неопубликованный.
     * Updating a comment by a user, can change the text and change the status to unpublished.
     */
    CommentDto updateCommentToEventFromUser(Long ownerId, Long eventId , Long comId, UpdateCommentDto updateCommentDto);

    /**
     * Получение у события своего комментария пользователем.
     * Receiving a comment from the event by the user.
     */
    CommentDto findCommentByIdFromUser(Long ownerId, Long eventId, Long comId);

    /**
     * Удаление у события своего комментария пользователем.
     * Removing a comment from an event by the user.
     */
    void deleteCommentByIdFromUser(Long ownerId, Long eventId, Long comId);

    /**
     * Поиск у события комментария по слову/тексту.
     * Search for a comment event by word/text.
     */
    List<CommentDto> searchCommentsByParamFromUser(Long ownerId, Long eventId, String text, Integer from, Integer size);

    /**
     * Получение комментария администратором.
     * Receiving a comment by an administrator.
     */
    CommentDto findCommentByIdFromAdmin(Long comId);

    /**
     * Изменение комментария администратором. Изменить может все.
     * Editing a comment by an admin. Everything can change.
     */
    CommentDto updateCommentByIdFromAdmin(Long comId, UpdateCommentByAdmin updateCommentByAdmin);

    /**
     * Удаление комментария администратором.
     * Delete comment by admin.
     */
    void deleteCommentByIdFromAdmin(Long comId);

    /**
     * Публикация комментария администратором.
     * Posting a comment by an admin.
     */
    CommentDto publishCommentByIdFromAdmin(Long comId);

    /**
     * Отклонение комментария администратором.
     * Rejecting a comment by an admin.
     */
    CommentDto rejectCommentByIdFromAdmin(Long comId);
}
