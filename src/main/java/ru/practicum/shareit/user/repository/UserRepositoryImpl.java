package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> users = new HashMap<>();
    private Set<String> emails = new HashSet<>();
    private Long id;

    @Override
    public User create(User user) {
        user.setId(getId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateById(Long id, User updatedUser) {
        users.put(id, updatedUser);
        return updatedUser;
    }

    @Override
    public User deleteById(Long id) {
        User deletedUser = users.remove(id);

        return deletedUser;
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean idIsExists(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean userIsExists(User user) {
        return users.containsValue(user);
    }

    @Override
    public void createEmail(String email) {
        emails.add(email);
    }

    @Override
    public void updateEmail(String oldEmail, String newEmail) {
        emails.remove(oldEmail);
        emails.add(newEmail);
    }

    @Override
    public void deleteEmail(String email) {
        emails.remove(email);
    }

    @Override
    public boolean emailIsExists(String email) throws NoSuchElementException {
        return emails.contains(email);
    }

    private Long getId() {
        if (id == null) {
            id = 0L;
        }
        return ++id;
    }
}
