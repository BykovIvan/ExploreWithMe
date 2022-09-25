package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.user.UserDto;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto getById(Long id);

    void deleteById(Long userId);
}
