package ru.bykov.explore.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 2000)
    private String annotation;
    @NotNull
    @Positive
    private Long category;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 7000)
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    @Positive
    private Long eventId;
    @NotNull
    private Boolean paid;
    @NotNull
    @PositiveOrZero
    private Long participantLimit;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 120)
    private String title;
}
