package ru.bykov.explore.model.dto.comment;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentByAdmin {
    @Positive
    @NotNull
    private Long event;
    @Positive
    @NotNull
    private Long owner;
    @NotNull
    @NotNull
    @Size(min = 1, max = 7000)
    private String text;
    private String status;
}
