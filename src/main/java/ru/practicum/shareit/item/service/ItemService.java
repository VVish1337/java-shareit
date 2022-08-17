package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsOfUser(long userId);

    List<ItemDto> getItemList();

    ItemDto updateItem(long userId,long itemId, ItemDto item);

    List<ItemDto> searchItem(String text);
}