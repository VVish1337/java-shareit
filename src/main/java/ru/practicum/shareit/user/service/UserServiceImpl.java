package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private User convertUser;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserDto userDto) {
        convertUser = userRepository.save(UserMapper.userDtoToUser(userDto,0));
        return convertUser;
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
    public User updateUser(long userId, UserDto userDto) {
        getUserById(userId);
        convertUser = userRepository.updateUser(UserMapper.userDtoToUser(userDto,userId));
        return convertUser;
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }
}