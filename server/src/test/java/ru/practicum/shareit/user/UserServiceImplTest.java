package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExists;
import ru.practicum.shareit.user.exception.UserIdNotFound;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("UserServiceImplTest должен ")
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @BeforeEach
    public void initEntity() {
        user = User.builder().id(1L).name("test_name_1").email("test_email_1").build();
    }

    @DisplayName("сохранять пользователя")
    @Test
    public void create_whenSuccessInvoked_thenCreatedUserIsReturned() {
        when(userRepository.save(any())).thenReturn(user);

        UserDto savedUserDto = UserMapper.toUserDto(user);
        UserDto returnedUserDto = userServiceImpl.create(savedUserDto);

        Assertions.assertEquals(savedUserDto, returnedUserDto);
    }

    @DisplayName("НЕ сохранять пользователя, если такой email уже существует в бд")
    @Test
    public void create_whenEmailAlreadyExist_thenCreatedUserIsNotReturned() {
        UserDto duplicateEmailUserDto = UserDto.builder().name("test_name_2").email("test_email_1").build();

        when(userRepository.save(any())).thenThrow(UserEmailAlreadyExists.class);

        Assertions.assertThrows(UserEmailAlreadyExists.class, () -> userServiceImpl.create(duplicateEmailUserDto));
    }

    @DisplayName("обновлять пользователя по полю \"id\"")
    @Test
    public void updateById_whenSuccessInvoked_thenUpdatedUserIsReturned() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto updatedUserDto = UserDto.builder().name("test_updated-name_1").email("test_updated-email_1").build();
        user.setName(updatedUserDto.getName());
        user.setEmail(updatedUserDto.getEmail());
        when(userRepository.save(any())).thenReturn(user);

        UserDto returnedUserDto = userServiceImpl.updateById(anyLong(), updatedUserDto);

        updatedUserDto.setId(user.getId());
        Assertions.assertEquals(updatedUserDto, returnedUserDto);
    }

    @DisplayName("НЕ обновлять пользователя по полю \"id\", если этот id не найден в бд")
    @Test
    public void updateById_whenIdNotFound_thenUpdatedUserIsNotReturned() {
        UserDto updatedUserDto = UserDto.builder().name("test_updated-name_1").email("test_updated-email_1").build();

        when(userRepository.findById(anyLong())).thenThrow(UserIdNotFound.class);

        Assertions.assertThrows(UserIdNotFound.class, () -> userServiceImpl.updateById(anyLong(), updatedUserDto));
    }

    @DisplayName("удалять пользователя по полю \"id\"")
    @Test
    public void deleteById_whenSuccessInvoked_thenStringResponseIsReturned() {
        doNothing().when(userRepository).deleteById(anyLong());

        String returnedResponse = userServiceImpl.deleteById(anyLong());
        String response = "⬛️ удален пользователь по id: 0";
        Assertions.assertEquals(response, returnedResponse);
    }

    @DisplayName("выдавать пользователя по полю \"id\"")
    @Test
    public void getById_whenSuccessInvoked_thenIssuedUserIsReturned() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto returnedUserDto = userServiceImpl.getById(anyLong());

        UserDto issuedUserDto = UserMapper.toUserDto(user);
        Assertions.assertEquals(issuedUserDto, returnedUserDto);
    }

    @DisplayName("НЕ выдавать пользователя по полю \"id\", если этот id не найден в бд")
    @Test
    public void getById_whenIdNotFound_thenIssuedUserIsNotReturned() {
        when(userRepository.findById(anyLong())).thenThrow(UserIdNotFound.class);

        Assertions.assertThrows(UserIdNotFound.class, () -> userServiceImpl.getById(anyLong()));
    }

    @DisplayName("выдавать всех пользователей")
    @Test
    public void getAll_whenSuccessInvoked_thenIssuedUsersIsReturned() {
        User secondUser = User.builder().id(2L).name("test_name_2").email("test_email_2").build();

        when(userRepository.findAll()).thenReturn(List.of(user, secondUser));

        List<UserDto> returnedUsersDto = userServiceImpl.getAll();

        List<UserDto> issuedUsersDto = UserMapper.toUsersDto(List.of(user, secondUser));
        Assertions.assertEquals(issuedUsersDto, returnedUsersDto);
    }

    @DisplayName("проверять пользователя на наличие в бд по полю \"id\" (результат: найден)")
    @Test
    public void idIsExists_whenSuccessInvoked_thenExceptionIsNotReturned() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        userServiceImpl.idIsExists(anyLong());
    }

    @DisplayName("проверять пользователя на наличие в бд по полю \"id\" (результат: НЕ найден)")
    @Test
    public void idIsExists_whenIdNotFound_thenExceptionIsReturned() {
        when(userRepository.existsById(anyLong())).thenThrow(UserIdNotFound.class);

        Assertions.assertThrows(UserIdNotFound.class, () -> userServiceImpl.idIsExists(anyLong()));
    }
}
