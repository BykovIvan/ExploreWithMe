package ru.bykov.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.User;
import ru.bykov.explore.services.UserService;

import javax.transaction.Transactional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Transactional
    public User create(@RequestBody User user) {
        log.info("Получен запрос к эндпоинту /admin/users. Метод POST");
        return userService.create(user);
    }

    @GetMapping("/{userId}")
    public User userById(@PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту /users получение по id. Метод GET");
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту /users удаление по id. Метод DELETE");
        userService.deleteById(userId);
    }
}
