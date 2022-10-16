package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.item.controller.ItemController.USER_ID_HEADER;

/**
 * // TODO .
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                                    @Valid @RequestBody ItemRequestPostDto dto) {
        return itemRequestClient.createItemRequest(userId, dto);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                                 @PathVariable long requestId) {
        return itemRequestClient.getItemRequest(userId, requestId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getItemRequestAll(@Min(0) @RequestParam(defaultValue = "0") int from,
                                                    @PositiveOrZero @RequestParam(defaultValue = "10") int size,
                                                    @RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestClient.getItemRequestAll(from, size, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestAllByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestClient.getItemRequestAllByUserId(userId);
    }
}