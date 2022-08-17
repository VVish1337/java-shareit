package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long,Item> items = new HashMap<>();
    private final ItemMapper itemMapper;
    private long count = 1;

    public ItemRepositoryImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto save(long userId,ItemDto itemDto) {
        Item item = itemMapper.dtoToItem(itemDto,userId);
        item.setId(counter());
        items.put(item.getId(),item);
        return itemMapper.itemToDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if(items.containsKey(itemId)){
            return itemMapper.itemToDto(items.get(itemId));
        }else{
            throw new IllegalStateException("Item don't exists");
        }
    }

    @Override
    public List<ItemDto> getItemsOfUser(long userId) {
        List<ItemDto> itemsDtoList = new ArrayList<>();
        for(Item item:items.values()){
            if(item.getOwner()==userId){
                itemsDtoList.add(itemMapper.itemToDto(item));
            }
        }
        return itemsDtoList;
    }

    @Override
    public List<ItemDto> getItemList() {
        List<ItemDto> itemsDtoList = new ArrayList<>();
        for(Item item:items.values()){
            itemsDtoList.add(itemMapper.itemToDto(item));
        }
        System.out.println(items);
        return itemsDtoList;
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        if(items.get(itemId).getOwner()!=userId){
            throw new IllegalStateException("Wrong userId");
        }
        Item item = itemMapper.dtoToItem(getItemById(itemId),userId);
        if(itemDto.getName()!=null){
            item.setName(itemDto.getName());
        }
        if(itemDto.getDescription()!=null){
            item.setDescription(itemDto.getDescription());
        }
        if(itemDto.getAvailable()!=null){
            item.setAvailable(itemDto.getAvailable());
        }
        System.out.println(item);
        items.put(item.getId(),item);
        return itemMapper.itemToDto(item);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        Set<ItemDto> itemsDtoList = new HashSet<>();
        for(Item item:items.values()){
            if(item.getAvailable()){
                if(item.getName().toLowerCase().contains(text.toLowerCase())){
                    itemsDtoList.add(itemMapper.itemToDto(item));
                }
                if(item.getDescription().toLowerCase().contains(text.toLowerCase())){
                    itemsDtoList.add(itemMapper.itemToDto(item));
                }
            }
        }
        return new ArrayList<>(itemsDtoList);
    }

    private long counter(){
        return count++;
    }
}
