package ru.bykov.explore.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.user.UserDto;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.utils.mappingForDto.UserMapping;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        @Valid User user = UserMapping.toUser(userDto);
        return UserMapping.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapping.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет такого пользователя!")));
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя не существует!"));
        userRepository.deleteById(userId);
    }
}
