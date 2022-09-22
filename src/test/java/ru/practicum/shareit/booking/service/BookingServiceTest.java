package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPatchResponseDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    private BookingService bookingService;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;

    private User user;
    private Item item;
    private User owner;
    private Booking booking;
    private BookingPostDto bookingPostDto;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        bookingPostDto = new BookingPostDto(1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        user = new User(1L, "name", "user@mail.ru");
        owner = new User(2L, "owner", "user2@mail.ru");
        item = new Item(
                1L,
                "name",
                "description",
                true,
                owner,
                1L);
        booking = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                item,
                user,
                BookingStatus.APPROVED);
    }

    @Test
    public void createBookingTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        BookingPostResponseDto result = bookingService.addBooking(1L, bookingPostDto);
        assertNotNull(result);
        assertEquals(bookingPostDto.getItemId(), result.getItem().getId());
        assertEquals(bookingPostDto.getStart(), result.getStart());
        assertEquals(bookingPostDto.getEnd(), result.getEnd());
    }

    @Test
    public void addBookingThrowItemUnavailable() {
        LocalDateTime date = LocalDateTime.now();
        bookingPostDto.setStart(date);
        bookingPostDto.setEnd(date.minusDays(1));
        ItemUnavailableException e = assertThrows(ItemUnavailableException.class,
                () -> bookingService.addBooking(1L, bookingPostDto));
        assertNotNull(e);
    }

    @Test
    public void createInvalidBookingTest() {
        item.setOwner(user);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(user.getId(), bookingPostDto));
        assertNotNull(e);
    }

    @Test
    public void findAllByBookerUnsupportedStatus() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        UnsupportedStatusException e = assertThrows(UnsupportedStatusException.class,
                () -> bookingService
                        .findAllByItemOwner("unsupported", 1L, 1, 10));
        assertNotNull(e);
    }

    @Test
    public void patchBookingTest() {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        BookingPatchResponseDto result = bookingService.updateBookingStatus(booking.getId(),
                true,
                item.getOwner().getId());
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    public void patchBookingItemThrowUnavailable() {
        booking.setStatus(BookingStatus.APPROVED);
        item.setOwner(owner);
        when(bookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(booking));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));
        ItemUnavailableException e = assertThrows(ItemUnavailableException.class,
                () -> bookingService.updateBookingStatus(1L, true, item.getOwner().getId()));
        assertNotNull(e);
    }

    @Test
    public void patchBookingThrowNotFound() {
        booking.setStatus(BookingStatus.WAITING);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, true, user.getId()));
        assertNotNull(e);
    }

    @Test
    public void findByIdTest() {
        item.setOwner(owner);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        BookingGetDto result = bookingService.getBooking(1L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void findAllByItemOwnerStateRejectedTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findRejectedBookingsByOwner(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllByItemOwner("REJECTED", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateWaitingTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findRejectedBookingsByOwner(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllByItemOwner("WAITING", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateCurrent() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findBookingsByItemOwnerCurrent(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllByItemOwner("CURRENT", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateFuture() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findByItemOwnerIdAndStartIsAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllByItemOwner("FUTURE", 1L, 1, 20);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStatePast() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findByItemOwnerIdAndEndIsBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllByItemOwner("PAST", 1L, 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }


    @Test
    public void findAllStateAll() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findByBookerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllBookings("ALL", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllStateWaiting() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findRejectedBookings(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllBookings("WAITING", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllStateRejected() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findRejectedBookings(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllBookings("REJECTED", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllStatePast() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findPastBookings(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllBookings("PAST", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllStateFuture() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findByBookerIdAndStartIsAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllBookings("FUTURE", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllStateCurrent() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(bookingRepository
                .findByBookerIdCurrent(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        List<BookingGetDto> result = bookingService.findAllBookings("CURRENT", user.getId(), 1, 10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}