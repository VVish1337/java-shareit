package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.item.marker.Create;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final String DEFAULT_STATE_VALUE = "ALL";
    private final BookingClient bookingClient;

    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestBody @Validated(Create.class) BookingPostDto bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        return bookingClient.addBooking(userId, bookingDto);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@PathVariable Long bookingId,
                                                      @RequestParam Boolean approved,
                                                      @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Update booking {}, approved={}. userId={}", bookingId, approved, userId);
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookings(@RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                                  @RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) int from,
                                                  @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.findAllBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByItemOwner(@RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                                     @RequestHeader(USER_ID_HEADER) Long userId,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        log.info("Get bookings owner with state{}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.findAllByItemOwner(state, userId, from, size);
    }
}