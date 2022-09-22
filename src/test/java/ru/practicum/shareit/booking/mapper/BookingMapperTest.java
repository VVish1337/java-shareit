package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private BookingPostDto bookingPostDto;
    private Item item;
    private User user;
    private Booking booking;
    private BookingPostResponseDto bookingPostResponseDto;
    private BookingGetDto bookingGetDto;
    private BookingPatchResponseDto bookingPatchResponseDto;
    private BookingInItemDto bookingInItemDto;

    @BeforeEach
    public void beforeEach() throws Exception {
        user = new User(1L, "name", "user@gmail.com");
        item = new Item(1L, "name", "true", true, user, 1L);
        bookingPostDto = new BookingPostDto(1L, item.getId(), LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        booking = new Booking(
                1L,
                bookingPostDto.getStart(),
                bookingPostDto.getEnd(),
                item, user,
                BookingStatus.APPROVED);
        bookingPostResponseDto = BookingPostResponseDto.builder()
                .id(1L)
                .item(item)
                .start(bookingPostDto.getStart())
                .end(bookingPostDto.getEnd())
                .build();
        bookingGetDto = new BookingGetDto(1L, bookingPostDto.getStart(),
                bookingPostDto.getEnd(), BookingStatus.APPROVED, user, item, item.getName());
        bookingPatchResponseDto = BookingPatchResponseDto.builder().id(1L)
                .status(BookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .name(item.getName()).build();
        bookingInItemDto = new BookingInItemDto(1L, user.getId(), bookingPostDto.getStart(),
                bookingPostDto.getEnd());
    }

    @Test
    void toBooking() {
        Booking bookingTest = BookingMapper.toBooking(user, item, bookingPostDto);
        assertEquals(booking.getItem(), bookingTest.getItem());
        assertEquals(booking.getBooker(), bookingTest.getBooker());
        assertEquals(booking.getStart(), bookingTest.getStart());
    }

    @Test
    void toPostResponseDto() {
        BookingPostResponseDto bookingTest = BookingMapper.toPostResponseDto(booking);
        assertEquals(bookingPostResponseDto.getStart(), bookingTest.getStart());
        assertEquals(bookingPostResponseDto.getEnd(), bookingTest.getEnd());
        assertEquals(bookingPostResponseDto.getItem(), bookingTest.getItem());
    }

    @Test
    void toBookingGetDto() {
        BookingGetDto bookingGetDtoTest = BookingMapper.toBookingGetDto(booking);
        assertEquals(bookingGetDto.getStart(), bookingGetDtoTest.getStart());
        assertEquals(bookingGetDto.getEnd(), bookingGetDtoTest.getEnd());
        assertEquals(bookingGetDto.getItem(), bookingGetDtoTest.getItem());
    }

    @Test
    void toBookingPatchResponseDto() {
        BookingPatchResponseDto bookingPatchResponseDtoTest = BookingMapper.toBookingPatchResponseDto(booking);
        assertEquals(bookingPatchResponseDto.getItem(), bookingPatchResponseDtoTest.getItem());
        assertEquals(bookingPatchResponseDto.getBooker(), bookingPatchResponseDtoTest.getBooker());
        assertEquals(bookingPatchResponseDto.getStatus(), bookingPatchResponseDtoTest.getStatus());
    }

    @Test
    void toBookingGetListDto() {
        List<BookingGetDto> testList = BookingMapper.toBookingGetListDto(Collections.singletonList(booking));
        assertEquals(Collections.singletonList(bookingGetDto).get(0).getBooker(), testList.get(0).getBooker());
        assertEquals(Collections.singletonList(bookingGetDto).get(0).getStatus(), testList.get(0).getStatus());
        assertEquals(Collections.singletonList(bookingGetDto).get(0).getName(), testList.get(0).getName());
    }

    @Test
    void bookingInItemDto() {
        BookingInItemDto bookingTest = BookingMapper.bookingInItemDto(booking);
        assertEquals(bookingInItemDto.getBookerId(), bookingTest.getBookerId());
        assertEquals(bookingInItemDto.getEnd(), bookingTest.getEnd());
        assertEquals(bookingInItemDto.getStart(), bookingTest.getStart());
    }
}