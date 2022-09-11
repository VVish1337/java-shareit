package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static Booking toBooking(User user, Item item, BookingPostDto dto) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
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
        return BookingGetDto.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .name(booking.getItem().getName())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .build();
    }

    public static BookingPatchResponseDto toBookingPatchResponseDto(Booking booking) {
        return BookingPatchResponseDto.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .name(booking.getItem().getName())
                .build();
    }

    public static List<BookingGetDto> toBookingGetListDto(List<Booking> booking) {
        return booking.stream()
                .map(BookingMapper::toBookingGetDto)
                .collect(Collectors.toList());
    }

    public static BookingInItemDto bookingInItemDto(Booking booking) {
        if (booking == null) return null;
        return BookingInItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}