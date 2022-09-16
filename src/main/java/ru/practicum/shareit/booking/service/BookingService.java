package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPatchResponseDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;

import java.util.List;

public interface BookingService {
    BookingPostResponseDto addBooking(long userId, BookingPostDto bookingDto);

    BookingGetDto getBooking(long bookingId, long userId);

    BookingPatchResponseDto updateBookingStatus(Long bookingId, Boolean approve, Long userId);

    List<BookingGetDto> findAllBookings(String state, Long userId, int from, int size);

    List<BookingGetDto> findAllByItemOwner(String state, Long userId,int from,int size);
}