package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user1", "user1@email.com");
    }

    @Test
    void createUserReturnSavedUser() {
        UserDto postDto = new UserDto();
        postDto.setName(user.getName());
        postDto.setEmail(user.getEmail());
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        User userSaved = userService.save(postDto);

        assertNotNull(userSaved);
        assertEquals(1L, userSaved.getId());
        assertEquals(postDto.getName(), userSaved.getName());
        assertEquals(postDto.getEmail(), userSaved.getEmail());
    }

    @Test
    void updateUserReturnUpdatedUser() {
        user.setName("updated name");
        UserDto postDto = UserMapper.userToDto(user);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user));
        User user = userService.updateUser(1L, postDto);
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals(postDto.getName(), user.getName());
    }

    @Test
    void getByIdReturnUserFromDb() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));
        User user = userService.getUserById(1L);
        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    void getAllUsersReturnList() {
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(user));
        List<User> list = userService.getUsersList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(user.getId(), list.get(0).getId());
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1L);
    }
}