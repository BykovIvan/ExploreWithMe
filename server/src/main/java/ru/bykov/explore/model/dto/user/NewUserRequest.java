package ru.bykov.explore.model.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 200)
    @Email()
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 200)
    private String name;
}
