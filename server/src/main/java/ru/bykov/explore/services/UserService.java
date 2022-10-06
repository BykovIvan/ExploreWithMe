package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.user.NewUserRequest;
import ru.bykov.explore.model.dto.user.UserDto;

import java.util.List;

public interface UserService {

    /**
     * Получение информации о пользователях
     * Getting information about users
     */
    List<UserDto> getByParamFromAdmin(Long[] ids, Integer from, Integer size);

    /**
     * Добавление нового пользователя
     * Adding a new user
     */
    UserDto createFromAdmin(NewUserRequest newUserRequest);

    /**
     * Удаление пользователя
     * Deleting a user
     */
    void deleteByIdFromAdmin(Long userId);


}
