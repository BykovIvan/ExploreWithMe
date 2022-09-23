package ru.bykov.explore.services;

import ru.bykov.explore.model.User;

public interface UserService {
    User create(User userDto);
    User getById(Long id);
    void deleteById(Long userId);
}
