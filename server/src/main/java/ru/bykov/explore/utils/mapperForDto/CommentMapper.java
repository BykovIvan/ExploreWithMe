package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Comment;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;

public class CommentMapper {

    public static Comment toComment(NewCommentDto newCommentDto){
        return Comment.builder()
                .build();
    }

    public static CommentDto toCommentDto(Comment comment){
        return CommentDto.builder()
                .build();
    }
}
