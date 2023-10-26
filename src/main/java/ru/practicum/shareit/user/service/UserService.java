package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto updateById(Long id, UserDto updatedUserDto);

    String deleteById(Long id);

    UserDto getById(Long id);

    List<UserDto> getAll();

    void idIsExists(Long id);
}
