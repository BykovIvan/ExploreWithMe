package ru.bykov.explore.model.dto.comment;

import lombok.*;
import ru.bykov.explore.model.dto.user.UserShortDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoForEvent {
    private UserShortDto owner;
    private String createdOn;
    private String text;
}
