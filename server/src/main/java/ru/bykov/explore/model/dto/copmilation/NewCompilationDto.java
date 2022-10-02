package ru.bykov.explore.model.dto.copmilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    //default: false
    private Boolean pinned;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 80)
    private String title;
}
