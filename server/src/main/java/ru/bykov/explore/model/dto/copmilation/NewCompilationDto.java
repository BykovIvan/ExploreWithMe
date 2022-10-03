package ru.bykov.explore.model.dto.copmilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    @OneToMany(fetch = FetchType.EAGER)
    private List<Long> events;
    @Column(nullable = false, columnDefinition = "false")
    private Boolean pinned;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 80)
    private String title;
}
