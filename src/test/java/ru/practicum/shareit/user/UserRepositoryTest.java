package ru.practicum.shareit.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("UserRepositoryTest должен ")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    User user = User.builder().name("test_name_1").email("test_email_1").build();

    @DisplayName("сохранять пользователя")
    @Test
    void save_whenSuccessInvoked_thenSavedUserIsReturned() {
        Assertions.assertNull(user.getId());
        User returnedUser = userRepository.save(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(user, returnedUser);
    }

    @DisplayName("НЕ сохранять пользователя, если поле \"name\" == null")
    @Test
    void save_whenNameIsNull_thenSavedUserIsNotReturned() {
        user.setName(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @DisplayName("НЕ сохранять пользователя, если поле \"email\" == null")
    @Test
    void save_whenEmailIsNull_thenSavedUserIsNotReturned() {
        user.setEmail(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @DisplayName("НЕ сохранять пользователя, если длина поля \"email\" > 100")
    @Test
    void save_whenEmailLengthIsMoreThen100_thenSavedUserIsNotReturned() {
        String email = "test_email_1";
        user.setEmail(email.repeat(50));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @DisplayName("НЕ сохранять пользователя, если поле \"email\" НЕ уникальное")
    @Test
    void save_whenEmailIsNotUnique_thenSavedUserIsNotReturned() {
        userRepository.save(user);
        User userOther = User.builder().name("test_name_2").email("test_email_1").build();

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(userOther));
    }

    @DisplayName("выдавать пользователя по полю \"email\"")
    @Test
    void findByEmail_whenSuccessInvoked_thenFoundUserIsReturned() {
        userRepository.save(user);
        Optional<User> returnedUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertEquals(user, returnedUser.get());
    }
}
