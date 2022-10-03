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
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    //    "yyyy-MM-dd HH:mm:ss"
    @Future
    private String eventDate;
    @NotNull
    @Positive
    private Long eventId;
    private Boolean paid;
    @Positive
    private Long participantLimit;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
