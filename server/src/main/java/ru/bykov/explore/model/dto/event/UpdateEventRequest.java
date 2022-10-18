package ru.bykov.explore.model.dto.event;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    @NotNull
    @Positive
    private Long eventId;
    private Boolean paid;
    private Long participantLimit;
    private String title;
}
