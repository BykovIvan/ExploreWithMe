package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.user.UserDto;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.UserService;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.mapperForDto.EventMapper;
import ru.bykov.explore.utils.mapperForDto.UserMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createFromAdmin(UserDto userDto) {
        @Valid User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getByParamFromAdmin(Long[] ids, Integer from, Integer size) {
        for (Long id : ids) {
            userRepository.findById(id).orElseThrow(() -> new NotFoundException("Такого пользователя не существует!"));
        }

        return userRepository.findByIdIn(ids, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByIdFromAdmin(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя не существует!"));
        userRepository.deleteById(userId);
    }
}