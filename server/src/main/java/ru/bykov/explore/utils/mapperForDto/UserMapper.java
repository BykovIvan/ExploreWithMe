package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.user.NewUserRequest;
import ru.bykov.explore.model.dto.user.UserDto;
import ru.bykov.explore.model.dto.user.UserShortDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }
}
