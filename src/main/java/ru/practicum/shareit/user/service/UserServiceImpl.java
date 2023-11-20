package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        String email = userDto.getEmail();

        User user = UserMapper.toUser(userDto);
        User createdUser;

        try {
            createdUser = userRepository.save(user);
        } catch (Throwable e) {
            throw new UserEmailAlreadyExists("почта \"" + email + "\" уже существует, придумайте другую почту");
        }

        log.info("🟩 создан пользователь: " + createdUser);
        return UserMapper.toUserDto(createdUser);
    }

    @Transactional
    @Override
    public UserDto updateById(long id, UserDto updatedUserDto) {
        String newEmail = updatedUserDto.getEmail();
        String newName = updatedUserDto.getName();

        User updatableUser = userRepository.findById(id).orElseThrow(() -> new UserIdNotFound("введен несуществующий id пользователя: " + id));
        String updatableEmail = updatableUser.getEmail();
        String updatableName = updatableUser.getName();

        if (newEmail != null && !updatableEmail.equals(newEmail)) {
            emailIsExists(newEmail);
            updatableUser.setEmail(newEmail);

            log.info("🟪 обновлено поле \"email\": " + updatableUser);
        }

        if (newName != null && !updatableName.equals(newName)) {
            updatableUser.setName(newName);

            log.info("🟪 обновлено поле \"name\": " + updatableUser);
        }

        return UserMapper.toUserDto(userRepository.save(updatableUser));
    }

    @Transactional
    @Override
    public String deleteById(long id) {
        userRepository.deleteById(id);

        String responseAndLogging;
        responseAndLogging = "⬛️ удален пользователь по id: " + id;

        log.info(responseAndLogging);
        return responseAndLogging;
    }

    @Override
    public UserDto getById(long id) {
        User issuedUser = userRepository.findById(id).orElseThrow(() -> new UserIdNotFound("введен несуществующий id пользователя: " + id));

        log.info("🟦 выдан пользователь: " + issuedUser);
        return UserMapper.toUserDto(issuedUser);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> issuedUsers = userRepository.findAll();

        if (!issuedUsers.isEmpty()) {

            log.info("🟦 выдан список пользователей: " + issuedUsers);
            return UserMapper.toUsersDto(issuedUsers);

        } else {

            log.info("🟦 список пользователей НЕ выдан: " + issuedUsers);
            throw new UserNoUsersExistsYet("база пользователей пуста, исправьте это: ➕👤");
        }
    }

    @Override
    public void idIsExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserIdNotFound("введен несуществующий id пользователя: " + id);
        }
    }

    private void emailIsExists(String email) throws NoSuchElementException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserEmailAlreadyExists("почта \"" + email + "\" уже существует, придумайте другую почту");
        }
    }
}
