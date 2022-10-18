package ru.bykov.explore.model.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @Positive
    @NotNull
    private Long id;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 200)
    private String name;
}
