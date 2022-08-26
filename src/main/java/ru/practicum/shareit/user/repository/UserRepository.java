package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    User getUserById(long id);

    List<User> getUsersList();

    User updateUser(User user);

    void deleteUser(long id);
}