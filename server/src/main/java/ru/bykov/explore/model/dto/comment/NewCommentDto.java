package ru.bykov.explore.model.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 7000)
    private String text;
}
