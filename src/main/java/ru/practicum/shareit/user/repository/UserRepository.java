package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User create(User user);

    User updateById(Long id, User user);

    void createEmail(String email);

    void updateEmail(String oldEmail, String newEmail);

    void deleteEmail(String email);

    User deleteById(Long id);

    User getById(Long id);

    List<User> getAll();

    boolean idIsExists(Long id);

    boolean userIsExists(User user);

    boolean emailIsExists(String email);
}
