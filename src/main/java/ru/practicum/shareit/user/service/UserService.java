package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto updateById(long id, UserDto updatedUserDto);

    String deleteById(long id);

    UserDto getById(long id);

    List<UserDto> getAll();

    void idIsExists(Long id);
}
