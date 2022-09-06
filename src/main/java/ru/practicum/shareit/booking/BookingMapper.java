package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(User user, Item item, BookingPostDto dto) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public static BookingPostDto toPostDto(Booking booking, long itemId) {
        BookingPostDto dto = new BookingPostDto();
        dto.setId(booking.getId());
        dto.setItemId(itemId);
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

    public static BookingPostResponseDto toPostResponseDto(Booking booking){
        BookingPostResponseDto dto = new BookingPostResponseDto();
        dto.setId(booking.getId());
        dto.setItem(booking.getItem());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

    public static BookingGetDto toBookingGetDto(Booking booking){
        BookingGetDto dto = new BookingGetDto();
        dto.setId(booking.getId());
        dto.setItemId(booking.getItem().getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setName(booking.getItem().getName());
        dto.setStatus(booking.getStatus());
        dto.setUserId(booking.getBooker().getId());
        return dto;
    }
}
