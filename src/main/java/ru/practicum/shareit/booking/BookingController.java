package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.item.marker.Create;

import static ru.practicum.shareit.item.controller.ItemController.USER_ID_HEADER;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingPostResponseDto addBooking(@Validated(Create.class) @RequestHeader(USER_ID_HEADER) long userId,
                                             BookingPostDto bookingDto){
        return bookingService.addBooking(userId,bookingDto);
    }

    @GetMapping("{bookingId}")
    public BookingGetDto getBooking(@PathVariable Long bookingId,
                                    @RequestHeader(USER_ID_HEADER) Long userId){
        return bookingService.getBooking(bookingId,userId);
    }
}