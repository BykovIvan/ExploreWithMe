package ru.bykov.explore.model.dto.comment;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentByAdmin {
    private Long event;
    private Long owner;
    private String text;
    private String status;
}
