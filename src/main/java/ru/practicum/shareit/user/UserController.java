package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @Autowired
    public UserController(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsersList() {
        return userService.getUsersList();
    }

    @PostMapping
    public User save(@Valid @RequestBody UserDto userDto) {
        return userService.save(mapper.userDtoToUser(userDto));
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable long userId,@RequestBody UserDto userDto) {
        return userService.updateUser(userId,mapper.userDtoToUser(userDto));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId){
        userService.deleteUser(userId);
    }
}
