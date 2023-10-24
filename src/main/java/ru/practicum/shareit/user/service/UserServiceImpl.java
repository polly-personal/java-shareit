package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public UserDto create(UserDto userDto) {
        String email = userDto.getEmail();
//        emailIsExists(email);
        User user = UserMapper.toUser(userDto);
//        if (!userRepository.userIsExists(user))) {
//            userRepository.createEmail(email);
//            User createdUser = userRepository.create(user);
        User createdUser;
        try {
            createdUser = userRepository.save(user);
        } catch (Throwable e) {
            throw new UserEmailAlreadyExistsException("почта \"" + email + "\" уже существует, придумайте другую почту");
        }

        log.info("🟩 создан пользователь: " + createdUser);

        return UserMapper.toUserDto(createdUser);
//        }
//        log.info("🟩🟧 пользователь НЕ создан: " + userDto);
//        throw new ThisUserAlreadyExistException("данный пользователь уже существует");
//    return null;
    }

    @Override
    public UserDto updateById(Long id, UserDto updatedUserDto) {
        String newEmail = updatedUserDto.getEmail();
        String newName = updatedUserDto.getName();

//        User updatableUser = UserMapper.toUser(getById(id));
        User updatableUser = userRepository.findById(id).orElseThrow(() ->
                new UserIdNotFoundException("введен несуществующий id пользователя: " + id));
//        updatableUser.setId(id);
        String updatableEmail = updatableUser.getEmail();
        String updatableName = updatableUser.getName();

        if (newEmail != null) {
            if (!updatableEmail.equals(newEmail)) {
                emailIsExists(newEmail);

//                userRepository.updateEmail(updatableEmail, newEmail);
                updatableUser.setEmail(newEmail);
//                log.info("🟪 обновлено поле \"email\": " + updatableUser);

            } else {
//                log.info("🟪🟧 поле \"email\" НЕ обновлено: " + updatableUser);
            }
        }

        if (newName != null) {
            if (!updatableName.equals(newName)) {
                updatableUser.setName(newName);
//                log.info("🟪 обновлено поле \"name\": " + updatableUser);
            } else {
//                log.info("🟪🟧 поле \"name\" НЕ обновлено: " + updatableUser);
            }
        }

//        User updatedUser = userRepository.updateById(id, updatableUser);
        User updatedUser = userRepository.save(updatableUser);

        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public String deleteById(Long id) {
//        User deletedUser = userRepository.deleteById(id);
        userRepository.deleteById(id);
//        userRepository.deleteEmail(deletedUser.getEmail());

        String responseAndLogging;
//        responseAndLogging = "⬛️ удален пользователь: " + deletedUser;
        responseAndLogging = "⬛️ удален пользователь по id: " + id;
        log.info(responseAndLogging);

        return responseAndLogging;
    }

    @Override
    public UserDto getById(Long id) {
//       User issuedUser = userRepository.getById(id);
        User issuedUser = userRepository.findById(id).orElseThrow(() ->
                new UserIdNotFoundException("введен несуществующий id пользователя: " + id));

//        if (issuedUser != null) {
        log.info("🟦 выдан пользователь: " + issuedUser);

        return UserMapper.toUserDto(issuedUser);

//        } else {
//            log.info("🟦 пользователь НЕ выдан: " + issuedUser);
//
//            throw new UserIdNotFoundException("введен несуществующий id пользователя: " + id);
//        }
    }


//    @Override
//    public Long getOwnerIdByItemIdOrThrow(Long itemId) {
//        Optional<Item> itemByItemId = itemRepository.getOptionalOwnerByItemId(itemId);
//        if (!itemByItemId.isEmpty()) {
//
//            return itemByItemId.get().getOwnerId();
//
////            return null;
//        }
//
//        throw new UserErrorIdNotFoundByItemIdException("по id вещи: " + itemId + ", пользователь не найден");
//    }


    @Override
    public List<UserDto> getAll() {
//        List<User> issuedUsers = userRepository.getAll();
        List<User> issuedUsers = userRepository.findAll();

        if (issuedUsers != null && !issuedUsers.isEmpty()) {
            log.info("🟦 выдан список пользователей: " + issuedUsers);

            return UserMapper.toUsersDto(issuedUsers);

        } else {
            log.info("🟦 список пользователей НЕ выдан: " + issuedUsers);

            throw new NoUsersExistsYet("база пользователей пуста, исправьте это: ➕👤");
        }
    }

    @Override
    public void idIsExists(Long id) {
//        if (id != null && !userRepository.idIsExists(id)) {
//            throw new UserIdNotFoundException("введен несуществующий id пользователя: " + id);
//        }
        if (!userRepository.existsById(id)) {
            throw new UserIdNotFoundException("введен несуществующий id пользователя: " + id);
        }
    }

    public void emailIsExists(String email) throws NoSuchElementException {
//        if (userRepository.emailIsExists(email)) {
//            throw new UserEmailAlreadyExistsException("почта \"" + email + "\" уже существует, придумайте другую почту");
//        }
//        Optional<User> user = userRepository.findByEmail(email);
//        System.out.println("🟥" + user.get());
//        System.out.println("🟥" + user.isEmpty());
//        System.out.println("🟥" + user.isPresent());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserEmailAlreadyExistsException("почта \"" + email + "\" уже существует, придумайте другую почту");
        }
    }
}
