package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPatchResponseDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.item.controller.ItemController.USER_ID_HEADER;

/**
 * // TODO .
 */

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String DEFAULT_STATE_VALUE = "ALL";
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingPostResponseDto addBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestBody BookingPostDto bookingDto) {
        return bookingService.addBooking(userId, bookingDto);
    }

    @GetMapping("{bookingId}")
    public BookingGetDto getBooking(@PathVariable Long bookingId,
                                    @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @PatchMapping("{bookingId}")
    public BookingPatchResponseDto updateBookingStatus(@PathVariable Long bookingId,
                                                       @RequestParam Boolean approved,
                                                       @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping
    public List<BookingGetDto> findAllBookings(@RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                               @RequestHeader(USER_ID_HEADER) Long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return bookingService.findAllBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingGetDto> findAllByItemOwner(@RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                                  @RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        return bookingService.findAllByItemOwner(state, userId, from, size);
    }
}