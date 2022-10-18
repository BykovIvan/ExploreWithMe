package ru.bykov.explore.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    private Boolean commentModeration;
}
