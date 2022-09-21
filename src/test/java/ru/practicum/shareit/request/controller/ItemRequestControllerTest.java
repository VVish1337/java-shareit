package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.dto.ItemRequestPostResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.controller.ItemController.USER_ID_HEADER;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mvc;

    @Test
    void createItemRequestPost200() throws Exception {
        ItemRequestPostDto dto = getItemRequestPostDto();
        when(itemRequestService.createItemRequest(anyLong() ,any(ItemRequestPostDto.class)))
                .thenReturn(getItemRequestPostResponseDto(dto));
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(getItemRequestPostResponseDto(dto)))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getItemRequestPostResponseDto(dto).getId()), Long.class))
                .andExpect(jsonPath("$.description", is(getItemRequestPostResponseDto(dto).getDescription())));

    }

    @Test
    void getItemRequestPost200() throws Exception {
        ItemRequestWithItemsDto dto = getItemRequestWithItemsDto();
        when(itemRequestService.getItemRequest(anyLong(),anyLong()))
                .thenReturn(getItemRequestWithItemsDto());
        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(dto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.items", is(dto.getItems()), List.class));
    }

    @Test
    void getItemRequestAllPost200() throws Exception{
        ItemRequestWithItemsDto dto = getItemRequestWithItemsDto();
        when(itemRequestService.getItemRequestAll(anyInt(),anyInt(),anyLong()))
                .thenReturn(Collections.singletonList(dto));
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(dto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(dto.getDescription())))
                .andExpect(jsonPath("$[0].items", is(dto.getItems()), List.class));
    }

    @Test
    void getItemRequestAllByUserIdPost200() throws Exception {
        ItemRequestWithItemsDto dto = getItemRequestWithItemsDto();
        when(itemRequestService.getItemRequestAllByUserId(anyLong()))
                .thenReturn(Collections.singletonList(dto));
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(dto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(dto.getDescription())))
                .andExpect(jsonPath("$[0].items", is(dto.getItems()), List.class));
    }

    private ItemRequestWithItemsDto getItemRequestWithItemsDto(){
        return ItemRequestWithItemsDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
    }

    private ItemRequestPostResponseDto getItemRequestPostResponseDto(ItemRequestPostDto dto){
      return   ItemRequestPostResponseDto.builder()
                .id(1L)
                .description(dto.getDescription())
                .created(LocalDateTime.now()).build();
    }

    private ItemRequestPostDto getItemRequestPostDto() {
        return new ItemRequestPostDto("description");
    }
}