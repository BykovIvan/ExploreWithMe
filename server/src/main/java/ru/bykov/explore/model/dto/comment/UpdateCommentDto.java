package ru.bykov.explore.model.dto.comment;

import lombok.*;
import ru.bykov.explore.utils.CommentState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 7000)
    private String text;
    private CommentState status;
}
