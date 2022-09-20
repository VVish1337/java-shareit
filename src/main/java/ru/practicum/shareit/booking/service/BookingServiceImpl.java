package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Sort sort = Sort.by("start").descending();
    private Pageable pageable;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingPostResponseDto addBooking(long userId, BookingPostDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ItemUnavailableException("Booking start is after end");
        }
        User user = checkUserExists(userId);
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

    @Override
    public BookingGetDto getBooking(long bookingId, long userId) {
        User user = checkUserExists(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!(booking.getBooker().equals(user) || booking.getItem().getOwner().equals(user))) {
            log.warn("Owner or booker not found");
            throw new NotFoundException("Owner or booker not found");
        }
        return BookingMapper.toBookingGetDto(booking);
    }

    @Override
    public BookingPatchResponseDto updateBookingStatus(Long bookingId, Boolean approve, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            log.warn("Wrong User:" + userId);
            throw new NotFoundException("Wrong User");
        }
        BookingStatus status = convertToStatus(approve);
        if (booking.getStatus().equals(status)) {
            log.warn("Item already approved");
            throw new ItemUnavailableException("Already approved");
        }
        booking.setStatus(status);
        bookingRepository.save(booking);
        return BookingMapper.toBookingPatchResponseDto(booking);
    }

    @Override
    public List<BookingGetDto> findAllBookings(String state, Long userId, int from, int size) {
        State status = parseState(state);
        LocalDateTime now = LocalDateTime.now();
        checkUserExists(userId);
        List<Booking> bookings;
        pageable = PageRequest.of(from / size, size, sort);
        switch (status) {
            case REJECTED:
                bookings = bookingRepository.findRejectedBookings(userId, BookingStatus.REJECTED, pageable).toList();
                break;
            case WAITING:
                bookings = bookingRepository.findRejectedBookings(userId, BookingStatus.WAITING, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdCurrent(userId, now, pageable).toList();
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, now, pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository.findPastBookings(userId, now, pageable).toList();
                break;
            case ALL:
                bookings = bookingRepository.findByBookerId(userId, pageable).toList();
                break;
            default:
                throw new IllegalArgumentException("state:");
        }
        return BookingMapper.toBookingGetListDto(bookings);
    }

    @Override
    public List<BookingGetDto> findAllByItemOwner(String state, Long userId, int from, int size) {
        State status = parseState(state);
        checkUserExists(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        pageable = PageRequest.of(from / size, size, sort);
        switch (status) {
            case REJECTED:
                bookings = bookingRepository
                        .findRejectedBookingsByOwner(userId, BookingStatus.REJECTED, pageable).toList();
                break;
            case WAITING:
                bookings = bookingRepository
                        .findRejectedBookingsByOwner(userId, BookingStatus.WAITING, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingsByItemOwnerCurrent(userId, now, pageable).toList();
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, now, pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, now, pageable).toList();
                break;
            case ALL:
                bookings = bookingRepository.findByItemOwnerId(userId, pageable).toList();
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
            log.warn("Unsupported status:" + state);
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

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}