package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.user.NewUserRequest;
import ru.bykov.explore.model.dto.user.UserDto;
import ru.bykov.explore.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserControllerAdmin {

    private final UserService userService;

    @GetMapping()
    public List<UserDto> userById(@RequestParam(value = "ids") Long[] ids,
                                  @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту /admin/users получение пользователей по параметрам. Метод GET");
        return userService.findByParamFromAdmin(ids, from, size);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Получен запрос к эндпоинту /admin/users. Метод POST");
        return userService.createFromAdmin(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту /admin/users удаление по id = {}. Метод DELETE", userId);
        userService.deleteByIdFromAdmin(userId);
    }
}
