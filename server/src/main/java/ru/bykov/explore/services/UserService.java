package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.user.NewUserRequest;
import ru.bykov.explore.model.dto.user.UserDto;

import java.util.List;

public interface UserService {

    /**
     * Получение информации о пользователях администратором.
     * Getting information about users by an administrator.
     */
    List<UserDto> findByParamFromAdmin(Long[] ids, Integer from, Integer size);

    /**
     * Добавление нового пользователя администратором.
     * Adding a new user by an administrator.
     */
    UserDto createFromAdmin(NewUserRequest newUserRequest);

    /**
     * Удаление пользователя администратором.
     * Deleting a user by an administrator.
     */
    void deleteByIdFromAdmin(Long userId);
}
