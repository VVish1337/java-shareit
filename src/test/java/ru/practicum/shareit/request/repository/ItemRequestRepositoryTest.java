package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private User requester;
    private ItemRequest itemRequest;

    @BeforeEach
    public void beforeEach() {
        requester = userRepository.save(new User(null, "requestor", "requestor@email.com"));
        itemRequest = itemRequestRepository.save(new ItemRequest(null, "request",
                requester, LocalDateTime.now()));
    }

    @Test
    public void findAllByRequesterId() {
        List<ItemRequest> result = itemRequestRepository.findAllByRequesterId(requester.getId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(itemRequest.getDescription(), result.get(0).getDescription());
        assertEquals(itemRequest.getRequester(), result.get(0).getRequester());
        assertEquals(itemRequest.getCreated(), result.get(0).getCreated());
    }
}