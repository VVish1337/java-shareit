package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long count = 1;

    @Override
    public Item save(Item item) {
        item.setId(counter());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new NotFoundException("Item with this id:" + itemId + " doesn't exists");
        }
    }

    @Override
    public List<Item> getItemList(long userId) {
        List<Item> itemsList = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner() == userId) {
                itemsList.add(item);
            }
        }
        return itemsList;
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        if (items.get(itemId).getOwner() != item.getOwner()) {
            throw new NotFoundException("Wrong userId");
        }
        Item oldItem = items.get(itemId);
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        items.put(oldItem.getId(), oldItem);
        return oldItem;
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> itemsList = new ArrayList<>();
        if (text.isBlank()) {
            return itemsList;
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    && item.getAvailable()) {
                itemsList.add(item);
            }
        }
        return itemsList;
    }

    private long counter() {
        return count++;
    }
}