package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemInRequestDtoTest {
    @Test
    void itemInRequestDto() {
        ItemInRequestDto dto = ItemInRequestDto.builder().id(1L)
                .name("name")
                .requestId(1L)
                .available(true)
                .description("description")
                .build();
        assertEquals(1L, dto.getId());
        assertEquals("name", dto.getName());
        assertEquals("description", dto.getDescription());
        assertEquals(true, dto.getAvailable());
        assertEquals(1L, dto.getRequestId());
    }
}