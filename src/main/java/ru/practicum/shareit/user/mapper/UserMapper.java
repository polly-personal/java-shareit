package ru.practicum.shareit.user.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getName());
        log.info("🔀 user: " + user + " сконвертирован в userDto: " + userDto);

        return userDto;
    }

    public static List<UserDto> toUsersDto(List<User> users) {
        List<UserDto> usersDto = users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());

        log.info("🔀 список users: " + users + " сконвертирован в usersDto: " + usersDto);

        return usersDto;
    }


    public static User toUser(UserDto userDto) {
        User user = new User(userDto.getId(), userDto.getEmail(), userDto.getName());
        log.info("🔀 userDto: " + userDto + " сконвертирован в user: " + user);

        return user;
    }

    public static List<User> toUsers(List<UserDto> usersDto) {
        List<User> users = usersDto.stream().map(UserMapper::toUser).collect(Collectors.toList());

        log.info("🔀 список usersDto: " + usersDto + " сконвертирован в users: " + users);

        return users;
    }
}
