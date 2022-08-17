package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        userRepository.getUserById(userId);
        return itemRepository.save(userId,itemDto);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsOfUser(long userId) {
        return itemRepository.getItemsOfUser(userId);
    }

    @Override
    public List<ItemDto> getItemList() {
        return itemRepository.getItemList();
    }

    @Override
    public ItemDto updateItem(long userId,long itemId,ItemDto itemDto) {
        itemRepository.getItemById(itemId);
        return itemRepository.updateItem(userId,itemId,itemDto);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemRepository.searchItem(text);
    }
}
