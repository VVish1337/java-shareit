package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        User user =userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        convertItem = itemRepository.save(ItemMapper.dtoToItem(itemDto,user));
        return ItemMapper.itemToDto(convertItem);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        convertItem = itemRepository.findById(itemId)
                .orElseThrow(()->new NotFoundException("Item not found"));
        return ItemMapper.itemToDto(convertItem);
    }

    @Override
    public List<ItemDto> getItemList(long userId) {
        convertList = itemRepository.findAll().stream()
                .filter(item->item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
        return ItemMapper.listItemToDtoList(convertList);
    }

    @Override
    public ItemDto updateItem(long userId,long itemId,ItemDto itemDto) {
//        itemRepository.getItemById(itemId);
//        convertItem = itemRepository.updateItem(itemId,ItemMapper.dtoToItem(itemDto,userId));
//        return ItemMapper.itemToDto(convertItem);
        return null;
    }

    @Override
    public List<ItemDto> searchItem(String text) {
//        convertList = itemRepository.findByText(text);
//        return ItemMapper.listItemToDtoList(convertList);
        return null;
    }
}