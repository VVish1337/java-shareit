package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User save(UserDto userDto);

    User getUserById(long id);

    List<User> getUsersList();

    User updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}