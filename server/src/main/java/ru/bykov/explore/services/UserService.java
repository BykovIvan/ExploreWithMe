package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto createFromAdmin(UserDto userDto);

    void deleteByIdFromAdmin(Long userId);

    List<UserDto> getByParamFromAdmin(Long[] ids, Integer from, Integer size);
}
