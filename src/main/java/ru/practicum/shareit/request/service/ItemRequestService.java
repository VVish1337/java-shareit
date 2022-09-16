package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.dto.ItemRequestPostResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestPostResponseDto createItemRequest(long userId, ItemRequestPostDto dto);

    ItemRequestWithItemsDto getItemRequest(long userId,long requestId);

    List<ItemRequestWithItemsDto> getItemRequestAll(PageRequest pageRequest, long userId);

    List<ItemRequestWithItemsDto> getItemRequestAllByUserId(long userId);
}