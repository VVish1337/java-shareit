package ru.practicum.shareit.item;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;

import java.util.List;


@RestController
@RequestMapping("/items")
public class ItemController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

//    @GetMapping
//    public List<ItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
//        return itemService.getItemsOfUser(userId);
//    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemList() {
        return itemService.getItemList();
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }

    @PostMapping
    public ItemDto saveItem(@RequestHeader(value = USER_ID_HEADER)
                            long userId,
                            @RequestBody
                            @Validated(Create.class)
                            ItemDto itemDto) {
        System.out.println(userId);
        return itemService.saveItem(userId,itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @Validated(Update.class)
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }
}
