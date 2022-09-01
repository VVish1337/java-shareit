package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item,Long> {
//    Item save(Item item);
//
//    Item getItemById(long itemId);
//
//    List<Item> getItemList(long userId);
//
//    Item updateItem(long itemId, Item item);
//
//    List<Item> searchItem(String text);
}