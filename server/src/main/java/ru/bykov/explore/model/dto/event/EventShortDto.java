package ru.bykov.explore.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.dto.category.NewCategoryDto;
import ru.bykov.explore.model.dto.user.UserShortDto;

import java.time.LocalDateTime;

/**
 * Класс который возвращается не зарегистрированным пользователям
 * The class that is returned to unregistered users
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private String annotation;
    private NewCategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
