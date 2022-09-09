package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public BookingPostResponseDto addBooking(long userId, BookingPostDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ItemUnavailableException("Booking start is after end");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!item.getAvailable()) {
            throw new ItemUnavailableException("item unavailable");
        }
        if (userId == item.getOwner().getId()) {
            throw new NotFoundException("Owner can't create booking");
        }
        Booking booking = BookingMapper.toBooking(user, item, bookingDto);
        bookingRepository.save(booking);
        return BookingMapper.toPostResponseDto(booking);
    }

    public BookingGetDto getBooking(long bookingId, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!(booking.getBooker().equals(user) || booking.getItem().getOwner().equals(user))) {
            throw new NotFoundException("Owner or booker not found");
        }
        return BookingMapper.toBookingGetDto(booking);
    }

    public BookingPatchResponseDto updateBookingStatus(Long bookingId, Boolean approve, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException("Wrong User");
        }
        BookingStatus status = convertToStatus(approve);

        if (booking.getStatus().equals(status)) {
            throw new ItemUnavailableException("Already approved");
        }
        booking.setStatus(status);

        bookingRepository.save(booking);
        return BookingMapper.toBookingPatchResponseDto(booking);
    }


    public List<BookingGetDto> findAllBookings(String state, Long userId) {
        State status = parseState(state);
        userRepository.findById(userId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by("start").descending();
        List<Booking> bookings;
        switch (status) {
            case REJECTED:
                bookings = bookingRepository.findRejectedBookings(userId, BookingStatus.REJECTED);
                break;
            case WAITING:
                bookings = bookingRepository.findRejectedBookings(userId, BookingStatus.WAITING);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdCurrent(userId, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, now, sort);
                break;
            case PAST:
                bookings = bookingRepository.findPastBookings(userId, now);
                break;
            case ALL:
                bookings = bookingRepository.findByBookerId(userId, sort);
                break;
            default:
                throw new IllegalArgumentException("state:");
        }
        return BookingMapper.toBookingGetListDto(bookings);
    }

    public List<BookingGetDto> findAllByItemOwner(String state, Long userId) {
        State status = parseState(state);
        //  checkIfUserExists(userId);
        userRepository.findById(userId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        Sort sort = Sort.by("start").descending();

        switch (status) {
            case REJECTED:
                bookings = bookingRepository.findRejectedBookingsByOwner(userId, BookingStatus.REJECTED);
                break;
            case WAITING:
                bookings = bookingRepository.findRejectedBookingsByOwner(userId, BookingStatus.WAITING);
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingsByItemOwnerCurrent(userId, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, now, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, now, sort);
                break;
            case ALL:
                bookings = bookingRepository.findByItemOwnerId(userId, sort);
                break;
            default:
                throw new IllegalArgumentException("state:");
        }
        return BookingMapper.toBookingGetListDto(bookings);
    }

    private State parseState(String state) {
        State status;
        try {
            status = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unsupported status");
        }
        return status;
    }

    private BookingStatus convertToStatus(Boolean status) {
        if (status) {
            return BookingStatus.APPROVED;
        } else {
            return BookingStatus.REJECTED;
        }
    }
}