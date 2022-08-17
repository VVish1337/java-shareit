package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
   User save(User user);

   User getUserById(long id);

   List<User> getUsersList();

   User updateUser(long userId,User user);

   void deleteUser(long userId);
}
