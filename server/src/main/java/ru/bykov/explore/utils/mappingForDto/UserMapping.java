package ru.bykov.explore.utils.mappingForDto;

import ru.bykov.explore.model.dto.user.UserDto;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.user.UserShortDto;

public class UserMapping {
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

    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
