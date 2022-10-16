package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void userToDto() {
        User user = new User(1L, "user1", "user1@mail.ru");
        UserDto dto = UserMapper.userToDto(user);

        assertEquals(user.getId(), 1L);
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void userDtoToUser() {
        UserDto dto = new UserDto("user1", "user1@mail.ru");
        User user = UserMapper.userDtoToUser(dto, 1L);
        assertEquals(1L, user.getId());
        assertEquals(dto.getName(),user.getName());
        assertEquals(dto.getEmail(),user.getEmail());
    }
}