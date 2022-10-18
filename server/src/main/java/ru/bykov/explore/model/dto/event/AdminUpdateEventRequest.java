package ru.bykov.explore.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.dto.LocationDto;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {
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
    @NotBlank
    private String eventDate;
    private LocationDto location;
    @NotNull
    private Boolean paid;
    @NotNull
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 120)
    private String title;
    private Boolean commentModeration;
}
