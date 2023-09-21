package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.*;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;
    private Map<Long, User> users;
    private Long id;

    @Override
    public UserDto create(User user) {
        if (users == null) {
            users = new HashMap<>();
        }

        if (!users.containsValue(user)) {
            user.setId(getId());

            Long id = user.getId();
            users.put(id, user);
            log.info("🟩 создан пользователь: " + user);

            return userMapper.toUserDto(user);
        }

        log.info("🟩🟧 пользователь НЕ создан: " + user);
        throw new ThisUserAlreadyExistException("данный пользователь уже существует");
    }

    @Override
    public UserDto updateById(Long id, User updatedUser) {
        String newEmail = updatedUser.getEmail();
        String newName = updatedUser.getName();

        User updatableUser = users.get(id);
        String updatableEmail = updatableUser.getEmail();
        String updatableName = updatableUser.getName();


        if (newEmail != null && !updatableEmail.equals(newEmail)) {
            emailAlreadyIsExists(newEmail);
            updatableUser.setEmail(newEmail);
            log.info("🟪 обновлено поле \"email\": " + updatableUser);
        }

        if (newName != null && !updatableName.equals(newName)) {
            updatableUser.setName(newName);
            log.info("🟪 обновлено поле \"name\": " + updatableUser);
        }

        return userMapper.toUserDto(updatableUser);
    }

    @Override
    public String deleteById(Long id) {
        String responseAndLogging = "⬛️ удален пользователь: " + users.get(id);
        log.info(responseAndLogging);
        users.remove(id);

        return responseAndLogging;
    }

    @Override
    public UserDto getById(Long id) {
        User user = users.get(id);
        log.info("🟦 выдан пользователь: " + user);

        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        if (users != null) {
            List<UserDto> allUsers = users.values()
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
            log.info("🟦 выдан список пользователей: " + allUsers);

            return allUsers;
        }
        throw new NoUsersExistsYet("ни один пользователь еще не добавлен, исправьте это: ➕👤");
    }

    @Override
    public void idIsExists(Long id) {
        if (users != null && !users.containsKey(id)) {
            throw new UserIdNotFoundException("введен несуществующий id пользователя: " + id);
        }
    }

    @Override
    public void emailAlreadyIsExists(String email) throws NoSuchElementException {
        if (users != null) {
            Optional<User> userByEmail = users.values()
                    .stream()
                    .filter(user -> user.getEmail().equals(email))
                    .findFirst();

            if (!userByEmail.isEmpty()) {
                throw new UserEmailAlreadyExistsException("почта \"" + email + "\" уже существует, придумайте другую почту");
            }
        }
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
