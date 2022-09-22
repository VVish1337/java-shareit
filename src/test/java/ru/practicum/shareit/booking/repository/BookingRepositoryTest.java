package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User booker;
    private Booking booking;

    @BeforeEach
    public void beforeEach() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = start.plusDays(7);
        BookingStatus bookingStatus = BookingStatus.APPROVED;
        User itemOwner = userRepository.save(new User(null, "user 1", "user1@gmail.com"));
        booker = userRepository.save(new User(null, "user 2", "user2@gmail.com"));
        Item item = itemRepository.save(
                new Item(1L,
                        "item 1",
                        "description",
                        true,
                        itemOwner,
                        null));
        booking = bookingRepository
                .save(new Booking(null, start, end, item, booker, bookingStatus));
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void findRejectedBookingsRejected() {
        Page<Booking> result = bookingRepository
                .findRejectedBookings(booker.getId(), BookingStatus.REJECTED, Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findRejectedBookingsWaiting() {
        Page<Booking> result = bookingRepository
                .findRejectedBookings(booker.getId(), BookingStatus.WAITING, Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPastBookings() {
        Page<Booking> result = bookingRepository
                .findRejectedBookings(booker.getId(), BookingStatus.WAITING, Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findBookingsByItemOwnerCurrent() {
        Page<Booking> result = bookingRepository
                .findBookingsByItemOwnerCurrent(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBookerIdCurrent() {
        Page<Booking> result = bookingRepository
                .findByBookerIdCurrent(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findRejectedBookingsByOwnerRejected() {
        Page<Booking> result = bookingRepository
                .findRejectedBookingsByOwner(booker.getId(), BookingStatus.REJECTED, Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findRejectedBookingsByOwnerWaiting() {
        Page<Booking> result = bookingRepository
                .findRejectedBookingsByOwner(booker.getId(), BookingStatus.WAITING, Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBookerIdAndStartIsAfter() {
        Page<Booking> result = bookingRepository
                .findByBookerIdAndStartIsAfter(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertEquals(booking, result.toList().get(0));
    }

    @Test
    void findByBookerId() {
        Page<Booking> result = bookingRepository
                .findByBookerId(booker.getId(), Pageable.unpaged());
        assertNotNull(result);
        assertEquals(booking, result.toList().get(0));
    }

    @Test
    void findByItemOwnerId() {
        Page<Booking> result = bookingRepository
                .findByItemOwnerId(booker.getId(), Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByItemOwnerIdAndStartIsAfter() {
        Page<Booking> result = bookingRepository
                .findByItemOwnerIdAndStartIsAfter(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByItemOwnerIdAndEndIsBefore() {
        Page<Booking> result = bookingRepository
                .findByItemOwnerIdAndEndIsBefore(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByItemIdAndEndBefore() {
        List<Booking> result = bookingRepository
                .findByItemIdAndEndBefore(booker.getId(), LocalDateTime.now(), Sort.unsorted());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByItemIdAndStartAfter() {
        List<Booking> result = bookingRepository
                .findByItemIdAndStartAfter(booker.getId(), LocalDateTime.now(), Sort.unsorted());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}