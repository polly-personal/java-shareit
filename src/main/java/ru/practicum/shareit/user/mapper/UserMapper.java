package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class UserMapper {
    public UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        log.info("🔀 user: " + user + " сконвертирован в userDto: " + userDto);
        return userDto;
    }

    public List<UserDto> toUsersDto(List<User> users) {
        List<UserDto> usersDto = users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());

        log.info("🔀 список users: " + users + " сконвертирован в usersDto: " + usersDto);
        return usersDto;
    }


    public User toUser(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();

        log.info("🔀 userDto: " + userDto + " сконвертирован в user: " + user);
        return user;
    }

    public List<User> toUsers(List<UserDto> usersDto) {
        List<User> users = usersDto.stream().map(UserMapper::toUser).collect(Collectors.toList());

        log.info("🔀 список usersDto: " + usersDto + " сконвертирован в users: " + users);
        return users;
    }
}
