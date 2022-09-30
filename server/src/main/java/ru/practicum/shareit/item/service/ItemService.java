package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId,long userId);

    List<ItemDto> getItemList(long userId,int from,int size);

    ItemDto updateItem(long userId, long itemId, ItemDto item);

    List<ItemDto> searchItem(String text,int from,int size);

    CommentDto createComment(CreateCommentDto commentDto, Long itemId, Long userId);
}