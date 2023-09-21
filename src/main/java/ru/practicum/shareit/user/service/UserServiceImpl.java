package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailIsEmptyException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(User user) {
        mailNotNull(user.getEmail());
        userRepository.emailAlreadyIsExists(user.getEmail());

        return userRepository.create(user);
    }

    @Override
    public UserDto updateById(Long id, User updatedUser) {
        userRepository.idIsExists(id);
        return userRepository.updateById(id, updatedUser);
    }

    @Override
    public String deleteById(Long id) {
        userRepository.idIsExists(id);
        return userRepository.deleteById(id);
    }

    @Override
    public UserDto getById(Long id) {
        userRepository.idIsExists(id);
        return userRepository.getById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll();
    }

    private void mailNotNull(String email) {
        if (email == null) {
            throw new UserEmailIsEmptyException("поле \"email\" должно быть заполнено");
        }
    }

}
