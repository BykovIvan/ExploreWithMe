package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    void deleteById(Long userId);

    List<UserDto> getByParam(Long[] ids, Integer from, Integer size);
}
