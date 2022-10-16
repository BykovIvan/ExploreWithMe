package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Comment;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.CommentDtoForEvent;
import ru.bykov.explore.model.dto.comment.CommentShortDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.user.UserShortDto;
import ru.bykov.explore.utils.CommentState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(NewCommentDto newCommentDto, User owner, Event event, LocalDateTime createdOn) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .owner(owner)
                .event(event)
                .createdOn(createdOn)
                .status(CommentState.PENDING)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .owner(comment.getOwner().getId())
                .event(comment.getEvent().getId())
                .status(comment.getStatus().toString())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(formatter))
                .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment, UserShortDto owner, EventShortDto event) {
        return CommentShortDto.builder()
                .owner(owner)
                .event(event)
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(formatter))
                .build();
    }

    public static CommentDtoForEvent toCommentDtoForEvent(Comment comment, UserShortDto owner) {
        return CommentDtoForEvent.builder()
                .owner(owner)
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(formatter))
                .build();
    }
}
