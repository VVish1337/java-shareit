package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPatchResponseDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.controller.ItemController.USER_ID_HEADER;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mvc;

    @Test
    void createBookingPost200() throws Exception {
        BookingPostDto bookingPostDto = getBookingPostDto();
        when(bookingService.addBooking(anyLong(), any(BookingPostDto.class)))
                .thenReturn(getBookingPostResponseDto());
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingPostDto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getBookingPostResponseDto().getId()), Long.class))
                .andExpect(jsonPath("$.item", is(getBookingPostResponseDto().getItem()), Item.class));
    }

    @Test
    void findByIdPost200() throws Exception {
        BookingGetDto responseDto = getBookingGetDto();
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(responseDto);
        mvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class));
    }

    @Test
    void updateBookingStatusPost200() throws Exception {
        BookingPatchResponseDto responseDto = getBookingPatchResponse();
        when(bookingService.updateBookingStatus(anyLong(), any(Boolean.class), anyLong()))
                .thenReturn(responseDto);
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class));
    }

    @Test
    void findAllBookingsPost200() throws Exception {
        BookingGetDto responseDto = getBookingGetDto();
        when(bookingService.findAllBookings(any(String.class), anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(responseDto));
        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(responseDto.getId()), Long.class));
    }

    @Test
    void findAllBookingsByItemOwnerPost200() throws Exception {
        BookingGetDto responseDto = getBookingGetDto();
        when(bookingService.findAllByItemOwner(any(String.class), anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(responseDto));
        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(responseDto.getId()), Long.class));
    }

    private BookingPostDto getBookingPostDto() {
        return BookingPostDto.builder()
                .id(1L)
                .itemId(1L)
                .start(null)
                .end(null).build();
    }

    private BookingPostResponseDto getBookingPostResponseDto() {
        BookingPostResponseDto bookingPostResponseDto = new BookingPostResponseDto();
        bookingPostResponseDto.setId(1L);
        bookingPostResponseDto.setItem(new Item(1L, "name", "name",
                true, null, null));
        return bookingPostResponseDto;
    }

    private BookingGetDto getBookingGetDto() {
        return BookingGetDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .status(BookingStatus.APPROVED)
                .booker(new User(1L, "name", "user@gmail.com"))
                .item(new Item(1L, "name", "name", true, null, null))
                .name("name").build();
    }

    private BookingPatchResponseDto getBookingPatchResponse() {
        return BookingPatchResponseDto.builder()
                .id(1L)
                .status(BookingStatus.APPROVED)
                .booker(new User(1L, "name", "user@gmail.com"))
                .item(new Item(1L, "name", "name", true, null, null))
                .build();
    }
}