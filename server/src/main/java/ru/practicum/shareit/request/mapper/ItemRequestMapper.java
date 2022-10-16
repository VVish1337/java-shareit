package ru.practicum.shareit.request.mapper;


import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.dto.ItemRequestPostResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(User user, ItemRequestPostDto dto, LocalDateTime now) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .requester(user)
                .created(now)
                .build();
    }

    public static ItemRequestPostResponseDto toItemRequestPostResponseDto(ItemRequest itemRequest) {
        return new ItemRequestPostResponseDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated());
    }

    public static ItemRequestWithItemsDto toItemRequestWithItemsDto(ItemRequest itemRequest,
                                                                    List<Item> items) {
        return ItemRequestWithItemsDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(ItemMapper.toItemInRequestDtoList(items, itemRequest))
                .build();
    }
}