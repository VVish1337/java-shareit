package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.dto.ItemRequestPostResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemRequestServiceTest {

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private ItemRequestService requestService;
    private ItemRequestRepository requestRepository;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        requestRepository = mock(ItemRequestRepository.class);
        requestService = new ItemRequestServiceImpl(
                requestRepository,
                userRepository,
                itemRepository);
        user = new User(1L, "name", "user@gmail.com");
        itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now());
    }

    @Test
    public void createRequest() {
        ItemRequestPostDto inputDto = new ItemRequestPostDto(itemRequest.getDescription());
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequestPostResponseDto responseDto = requestService.createItemRequest(1L, inputDto);
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals(inputDto.getDescription(), responseDto.getDescription());
    }

    @Test
    void findAllByUserId() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(requestRepository
                .findAllByRequesterId(anyLong()))
                .thenReturn(new ArrayList<>());
        when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(new ArrayList<>());
        List<ItemRequestWithItemsDto> result = requestService.getItemRequestAllByUserId(1L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(requestRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(new ArrayList<>());
        List<ItemRequestWithItemsDto> result = requestService.getItemRequestAll(1, 10, 1L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(requestRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.findAllByRequestId(any(Long.class)))
                .thenReturn(new ArrayList<>());
        ItemRequestWithItemsDto result = requestService.getItemRequest(1L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }
}