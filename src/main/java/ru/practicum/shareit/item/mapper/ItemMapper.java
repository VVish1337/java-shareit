package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemMapper {
    ItemDto itemToDto(Item item);

    Item dtoToItem(ItemDto itemDto,long ownerId);
}
