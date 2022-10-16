package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto userToDto(User user) {
        return new UserDto(user.getName(),
                user.getEmail());
    }

    public static User userDtoToUser(UserDto userDto, long userId) {
        return new User(userId,
                userDto.getName(),
                userDto.getEmail());
    }
}