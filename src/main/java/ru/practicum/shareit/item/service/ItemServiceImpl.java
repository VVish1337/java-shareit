package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Item convertItem;
    private List<Item> convertList;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        userRepository.findById(userId);
        convertItem = itemRepository.save(ItemMapper.dtoToItem(itemDto,userId));
        return ItemMapper.itemToDto(convertItem);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        convertItem = itemRepository.getItemById(itemId);
        return ItemMapper.itemToDto(convertItem);
    }

    @Override
    public List<ItemDto> getItemList(long userId) {
        convertList = itemRepository.getItemList(userId);
        return ItemMapper.listItemToDtoList(convertList);
    }

    @Override
    public ItemDto updateItem(long userId,long itemId,ItemDto itemDto) {
        itemRepository.getItemById(itemId);
        convertItem = itemRepository.updateItem(itemId,ItemMapper.dtoToItem(itemDto,userId));
        return ItemMapper.itemToDto(convertItem);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        convertList = itemRepository.searchItem(text);
        return ItemMapper.listItemToDtoList(convertList);
    }
}