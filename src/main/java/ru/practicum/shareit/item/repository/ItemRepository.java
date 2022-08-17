package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    ItemDto save(long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsOfUser(long userId);

    List<ItemDto> getItemList();

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    List<ItemDto> searchItem(String text);
}
