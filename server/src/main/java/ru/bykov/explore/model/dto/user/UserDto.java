package ru.bykov.explore.model.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private Long id;
    private String name;

}
