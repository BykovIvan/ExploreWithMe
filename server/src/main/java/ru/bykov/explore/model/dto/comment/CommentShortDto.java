package ru.bykov.explore.model.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {
    private User owner;
    private String createdOn;
    private String text;
}
