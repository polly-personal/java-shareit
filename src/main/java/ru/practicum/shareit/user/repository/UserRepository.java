package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    UserDto create(User user);

    UserDto updateById(Long id, User user);

    String deleteById(Long id);

    UserDto getById(Long id);

    List<UserDto> getAll();

    void idIsExists(Long id);

    void emailAlreadyIsExists(String email);
}
