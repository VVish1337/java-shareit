package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingInItemDtoTest {
    @Test
    void bookingInItemDtoTest() {
        BookingInItemDto bookingInItemDto = new BookingInItemDto();
        bookingInItemDto.setId(1L);
        bookingInItemDto.setBookerId(1L);
        assertEquals(1L, bookingInItemDto.getId());
        assertEquals(1L, bookingInItemDto.getBookerId());
    }
}