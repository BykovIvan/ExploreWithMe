package ru.bykov.explore.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull
    @NotBlank
    private Float lat;
    @NotNull
    @NotBlank
    private Float lon;
}
