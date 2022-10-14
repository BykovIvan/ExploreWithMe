package ru.bykov.explore.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
//    @NotNull
//    @NotBlank
//    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
//    @NotNull
//    @NotBlank
//    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    @NotNull
    @Positive
    private Long eventId;
    private Boolean paid;
//    @NotNull
//    @Positive
    private Long participantLimit;
//    @NotNull
//    @NotBlank
//    @Size(min = 3, max = 120)
    private String title;
}
