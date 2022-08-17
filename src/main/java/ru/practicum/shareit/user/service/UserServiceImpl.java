package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getUsersList() {
        return userRepository.getUsersList();
    }

    @Override
    public User updateUser(long userId, User user) {
        getUserById(userId);
        user.setId(userId);
        return userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }
}
