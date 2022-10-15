package ru.bykov.explore.model.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.User;
import ru.bykov.explore.utils.CommentState;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentByAdmin {
    private Event event;
    private User owner;
    private String text;
    private CommentState status;
}
