package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    Item getItemById(long itemId);

    List<Item> getItemList(long userId);

    Item updateItem(long itemId, Item item);

    List<Item> searchItem(String text);
}