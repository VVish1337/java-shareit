package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.dto.ItemRequestPostResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ItemRequestMapperTest {

    private ItemRequest itemRequest;
    private ItemRequestPostDto itemRequestPostDto;
    private User user;
    private ItemInRequestDto itemInRequestDto;
    private Item item;


    @BeforeEach
    public void beforeEach() {
        item = new Item(1L, "name", "description", null, null, 1L);
        itemInRequestDto = new ItemInRequestDto(1L, "name", "description", true, 1L);
        User user = new User(1L, "name", "user@gmail.com");
        itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now());
        itemRequestPostDto = new ItemRequestPostDto("description");
    }

    @Test
    public void toModelTest() {
        ItemRequest itemRequest1 = ItemRequestMapper.toItemRequest(user, itemRequestPostDto, LocalDateTime.now());
        assertNotNull(itemRequest1);
        assertEquals(itemRequestPostDto.getDescription(), itemRequest1.getDescription());
    }

    @Test
    void toItemRequestPostResponseDto() {
        ItemRequestPostResponseDto itemResponseTest = ItemRequestMapper.toItemRequestPostResponseDto(itemRequest);
        ItemRequestPostResponseDto itemResponse = new ItemRequestPostResponseDto(1L,
                itemRequest.getDescription(), itemRequest.getCreated());
        assertEquals(itemResponse, itemResponseTest);
    }

    @Test
    void toItemRequestWithItemsDto() {
        ItemRequestWithItemsDto requestWithItemsDto = new ItemRequestWithItemsDto(
                1L,
                "description",
                itemRequest.getCreated(), Collections.singletonList(itemInRequestDto));
        ItemRequestWithItemsDto requestWithItemsDtoTest = ItemRequestMapper.toItemRequestWithItemsDto(
                itemRequest, Collections.singletonList(item));
        assertEquals(requestWithItemsDto.getId(), requestWithItemsDtoTest.getId());
        assertEquals(requestWithItemsDto.getItems().size(), requestWithItemsDtoTest.getItems().size());
    }
}