package ru.bykov.explore.model.dto.comment;

import lombok.*;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.user.UserShortDto;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {
    private UserShortDto owner;
    private EventShortDto event;
    private String createdOn;
    private String text;
}
