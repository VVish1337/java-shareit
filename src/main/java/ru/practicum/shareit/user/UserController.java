package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @GetMapping
    public User getUserById(@RequestHeader("X-Later-User-Id") long userId) {
        return null;
    }

    @GetMapping
    public List<User> getUsersList() {
        return null;
    }

    @PostMapping
    public User save(@RequestBody User user) {
        return null;
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable long userId) {
        return null;
    }
}
