package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository requestRepository;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        User itemOwner = userRepository.save(new User(1L, "owner", "owner@email"));
        User user = userRepository.save(new User(2L, "user", "user@email"));
        itemRequest = requestRepository.save(new ItemRequest(
                1L,
                "description",
                user,
                LocalDateTime.now()
        ));
        item = itemRepository.save(new Item(
                1,
                "item",
                "description",
                true,
                itemOwner,
                itemRequest.getId()));
    }

    @Test
    void search() {
        List<Item> testList = new ArrayList<>();
        testList.add(item);
        List<Item> searchedItemList = itemRepository.search("item", Pageable.unpaged()).toList();
        assertNotNull(searchedItemList);
        assertEquals(testList, searchedItemList);
        assertEquals(testList.get(0), searchedItemList.get(0));
    }

    @Test
    void findAllByRequestId() {
        List<Item> testList = new ArrayList<>();
        testList.add(item);
        List<Item> foundList = itemRepository.findAllByRequestId(itemRequest.getId());
        assertNotNull(foundList);
        assertEquals(testList, foundList);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
    }
}