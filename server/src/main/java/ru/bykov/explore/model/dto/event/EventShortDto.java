package ru.bykov.explore.model.dto.event;

import lombok.*;
import ru.bykov.explore.model.dto.category.CategoryDto;
import ru.bykov.explore.model.dto.user.UserShortDto;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
