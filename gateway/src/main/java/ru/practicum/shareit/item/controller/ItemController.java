package ru.practicum.shareit.item.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.marker.Create;
import ru.practicum.shareit.item.marker.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;


@Validated
@RestController
@RequestMapping("/items")
public class ItemController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId, @RequestHeader(USER_ID_HEADER) long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemList(@RequestHeader(USER_ID_HEADER) long userId,
                                              @Min(0) @RequestParam(defaultValue = "0") int from,
                                              @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        return itemClient.getItemList(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestParam String text,
                                             @Min(0) @RequestParam(defaultValue = "0") int from,
                                             @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        return itemClient.searchItem(userId,text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader(USER_ID_HEADER)
                                           long userId,
                                           @RequestBody @Validated(Create.class) ItemDto itemDto) {
        return itemClient.saveItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable long itemId,
                                             @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Validated({Create.class}) @RequestBody CreateCommentDto commentDto,
                                                @PathVariable Long itemId,
                                                @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.createComment(commentDto, itemId, userId);
    }
}