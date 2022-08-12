package ru.practicum.shareit.mappers;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

interface ItemMapper {
    ItemDto mapToItemDto(Item item);

    Item mapToItem(ItemDto itemDto);
}
