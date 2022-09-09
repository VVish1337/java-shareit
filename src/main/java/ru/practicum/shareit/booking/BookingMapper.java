package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

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

    public static BookingPostResponseDto toPostResponseDto(Booking booking) {
        BookingPostResponseDto dto = new BookingPostResponseDto();
        dto.setId(booking.getId());
        dto.setItem(booking.getItem());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

    public static BookingGetDto toBookingGetDto(Booking booking) {
        BookingGetDto dto = new BookingGetDto();
        dto.setId(booking.getId());
        dto.setItem(booking.getItem());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setName(booking.getItem().getName());
        dto.setStatus(booking.getStatus());
        dto.setBooker(booking.getBooker());
        return dto;
    }

    public static BookingPatchResponseDto toBookingPatchResponseDto(Booking booking) {
        BookingPatchResponseDto dto = new BookingPatchResponseDto();
        dto.setStatus(booking.getStatus());
        dto.setId(booking.getId());
        dto.setBooker(booking.getBooker());
        dto.setItem(booking.getItem());
        dto.setName(booking.getItem().getName());
        return dto;
    }

    public static List<BookingGetDto> toBookingGetListDto(List<Booking> booking) {
        return booking.stream()
                .map(BookingMapper::toBookingGetDto)
                .collect(Collectors.toList());
    }

    public static BookingInItemDto bookingInItemDto(Booking booking) {
        if (booking == null) return null;
        BookingInItemDto dto = new BookingInItemDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }
}
