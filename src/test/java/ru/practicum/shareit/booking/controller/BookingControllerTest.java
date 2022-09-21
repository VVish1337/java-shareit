package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void createBookingTest() throws Exception {
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

//    @Test
//    public void findByIdTest() throws Exception {
//        BookingDetailedDto responseDto = generateDetailedDto(ID);
//
//        when(bookingService.findById(any(Long.class), any(Long.class)))
//                .thenReturn(responseDto);
//
//        mvc.perform(get("/bookings/1")
//                        .header(USER_ID_HEADER, ID))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class));
//
//        verify(bookingService, times(1))
//                .findById(any(Long.class), any(Long.class));
//    }

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
        bookingPostResponseDto.setItem(new Item(1L, "name", "name", true, null, null));
        return bookingPostResponseDto;
    }
}