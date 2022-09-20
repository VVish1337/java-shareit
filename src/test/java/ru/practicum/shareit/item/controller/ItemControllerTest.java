package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.item.controller.ItemController.USER_ID_HEADER;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;


    @Test
    void itemCreatePost200andReturnItem() throws Exception {
        ItemDto itemDto = getItemDto();
        ItemDto responseItemDto = getResponseItemDto();
        when(itemService.saveItem(anyLong(), any(ItemDto.class)))
                .thenReturn(getResponseItemDto());
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseItemDto.getName())))
                .andExpect(jsonPath("$.description", is(responseItemDto.getDescription())));
    }

    @Test
    void itemUpdatePost200andReturnUpdatedItem() throws Exception {
        ItemDto itemDto = getItemDto();
        itemDto.setName("update");
        ItemDto updatedItem = getResponseItemDto();
        updatedItem.setName("update");
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(updatedItem);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updatedItem.getName())))
                .andExpect(jsonPath("$.description", is(updatedItem.getDescription())));
    }

    @Test
    void itemGetItemByIdPost200AndReturnItem() throws Exception {
        itemService.saveItem(1L, getItemDto());
        ItemDto itemResponse = getResponseItemDto();
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemResponse);
        mvc.perform(get("/items/1")
                        .content(mapper.writeValueAsString(itemResponse))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponse.getName())))
                .andExpect(jsonPath("$.description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$.available", is(itemResponse.getAvailable())));
    }

    @Test
    void itemGetItemListPost200AndReturnList() throws Exception {
        itemService.saveItem(1L, getItemDto());
        ItemDto itemResponse = getResponseItemDto();
        ArrayList<ItemDto> list = new ArrayList<>();
        list.add(itemResponse);
        when(itemService.getItemList(anyLong(), anyInt(), anyInt()))
                .thenReturn(list);
        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(itemResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemResponse.getName())))
                .andExpect(jsonPath("$[0].description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemResponse.getAvailable())));
    }

    @Test
    void createCommentPost200AndReturnCommentDto() throws Exception {
        CreateCommentDto inputCommentDto = new CreateCommentDto("some text");
        CommentDto responseCommentDto = getResponseCommentDto(inputCommentDto);
        when(itemService.createComment(any(CreateCommentDto.class), any(Long.class), any(Long.class)))
                .thenReturn(responseCommentDto);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(inputCommentDto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseCommentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(responseCommentDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(responseCommentDto.getText())));
    }

    @Test
    void searchItemPost200() throws Exception{
        when(itemService.searchItem(anyString(),anyInt(),anyInt()))
                .thenReturn(new ArrayList<>());
        mvc.perform(get("/items/search")
                        .param("text","text value")
                        .param("from","1")
                        .param("size","20"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    private ItemDto getItemDto() {
        return ItemDto.builder()
                .name("item")
                .description("item description")
                .available(true)
                .build();
    }

    private ItemDto getResponseItemDto() {
        return ItemDto.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .available(true)
                .build();
    }

    private CommentDto getResponseCommentDto(CreateCommentDto dto) {
        return CommentDto.builder()
                .id(1L)
                .authorName("name")
                .text(dto.getText())
                .build();
    }
}