package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.UpdateCommentByAdmin;
import ru.bykov.explore.services.CommentService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comment")
public class CommentControllerAdmin {

    private final CommentService commentService;

    @GetMapping("/{comId}")
    public CommentDto findComment(@PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /admin/comment/{comId}/publish получение комментария id = {} администратором. Метод GET.", comId);
        return commentService.findCommentByIdFromAdmin(comId);
    }

    @PatchMapping("/{comId}")
    public CommentDto updateComment(@PathVariable("comId") Long comId,
                                    @RequestBody UpdateCommentByAdmin updateCommentByAdmin) {
        log.info("Получен запрос к эндпоинту /admin/comment/{comId}/publish обновление комментария id = {} администратором. Метод PATCH.", comId);
        return commentService.updateCommentByIdFromAdmin(comId, updateCommentByAdmin);
    }

    @DeleteMapping("/{comId}")
    public void deleteComment(@PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /admin/comment/{comId}/publish удаление комментария id = {} администратором. Метод DELETE.", comId);
        commentService.deleteCommentByIdFromAdmin(comId);
    }

    @PatchMapping("/{comId}/cancel")
    public CommentDto publishComment(@PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /admin/comment/{comId}/cancel получение комментария id = {} администратором. Метод PATCH.", comId);
        return commentService.cancelCommentByIdFromAdmin(comId);
    }

    @PatchMapping("/{comId}/publish")
    public CommentDto rejectComment(@PathVariable("comId") Long comId) {
        log.info("Получен запрос к эндпоинту /admin/comment/{comId}/publish отклонение комментария id = {} администратором. Метод PATCH.", comId);
        return commentService.publishCommentByIdFromAdmin(comId);
    }


}
