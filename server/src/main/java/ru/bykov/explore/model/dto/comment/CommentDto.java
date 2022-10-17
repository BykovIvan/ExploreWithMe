package ru.bykov.explore.model.dto.comment;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long event;
    private Long owner;
    private String createdOn;
    private String status;
    private String text;
}