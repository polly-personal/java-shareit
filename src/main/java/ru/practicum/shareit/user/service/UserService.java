package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(User user);

    UserDto updateById(Long id, User user);

    String deleteById(Long id);

    UserDto getById(Long id);

    List<UserDto> getAll();
}
