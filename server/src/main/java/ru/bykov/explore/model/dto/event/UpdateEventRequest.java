package ru.bykov.explore.model.dto.event;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 255)
    private String description;
    @NotNull
    @NotBlank
    private String eventDate;
    @NotNull
    @Positive
    private Long eventId;
    @NotNull
    private Boolean paid;
    @NotNull
    private Long participantLimit;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 120)
    private String title;
}
