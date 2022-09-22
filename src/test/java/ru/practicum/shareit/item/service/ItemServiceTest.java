package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ItemServiceTest {
    private ItemService itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;
    private Item item;
    private User user;
    private ItemDto itemDto;
    private Comment comment;
    private Booking booking;
    private CreateCommentDto createCommentDto;
    private ItemRequest itemRequest;

    @BeforeEach
    public void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository, itemRequestRepository);
        user = new User(1L, "name", "user@mail.ru");
        item = new Item(
                1L,
                "name",
                "description",
                true,
                user,
                2L);
        itemDto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                null,
                null,
                null,
                2L);
        comment = new Comment(1L, "comment", item, user, LocalDateTime.now());
        createCommentDto = new CreateCommentDto("comment");
        booking = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        itemRequest = new ItemRequest(
                1L, "description", user, LocalDateTime.now()
        );
    }

    @Test
    void createItem() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        ItemDto result = itemService.saveItem(1L, itemDto);
        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(itemDto.getRequestId(), result.getRequestId());
    }


    @Test
    void createItemThrowNotFoundException() {
        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> itemService.saveItem(2L, itemDto));
        assertNotNull(e);
    }

    @Test
    void updateItemThrowNotFoundException() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(2L, 1L, itemDto));
        assertNotNull(e);
    }

    @Test
    void createComment() {
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findSuitableBookingsForComments(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        CommentDto result = itemService.createComment(createCommentDto, item.getId(), user.getId());
        assertNotNull(result);
        assertEquals(createCommentDto.getText(), result.getText());
        assertEquals(user.getName(), result.getAuthorName());
    }

    @Test
    void createCommentThrowException() {
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findSuitableBookingsForComments(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        ItemUnavailableException result = assertThrows(ItemUnavailableException.class,
                () -> itemService.createComment(createCommentDto, item.getId(), user.getId()));
        assertNotNull(result);
    }

    @Test
    void createCommentThrowNotFoundException() {
        createCommentDto.setText("");
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findSuitableBookingsForComments(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        NotFoundException result = assertThrows(NotFoundException.class,
                () -> itemService.createComment(createCommentDto, item.getId(), user.getId()));
        assertNotNull(result);
    }

    @Test
    public void updateItem() {
        itemDto.setName("updatedName");
        item.setName("updatedName");
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(commentRepository.findByItemId(anyLong()))
                .thenReturn(new ArrayList<>());
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), itemDto);
        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
    }

    @Test
    public void updateItemThrowNotFound() {
        item.setOwner(user);
        when(commentRepository.findByItemId(any(Long.class)))
                .thenReturn(new ArrayList<>());
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(user.getId(), itemDto.getId(), itemDto));
        assertNotNull(exception);
    }

    @Test
    public void findItemById() {
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));
        when(commentRepository.findByItemId(any(Long.class)))
                .thenReturn(new ArrayList<>());
        ItemDto result = itemService.getItemById(item.getId(), 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    public void findAllItems() {
        when(itemRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        List<ItemDto> result = itemService.getItemList(1L, 1, 20);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findItemsByRequest() {
        when(itemRepository.search(any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        List<ItemDto> result = itemService.searchItem("item", 1, 10);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


}