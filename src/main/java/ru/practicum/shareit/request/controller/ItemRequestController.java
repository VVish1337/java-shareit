package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.dto.ItemRequestPostResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.shareit.item.controller.ItemController.USER_ID_HEADER;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final Sort sort = Sort.by("created").descending();

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestPostResponseDto createItemRequest( @RequestHeader(USER_ID_HEADER) long userId,
                                                         @Valid @RequestBody ItemRequestPostDto dto) {
        return itemRequestService.createItemRequest(userId, dto);
    }

    @GetMapping("{requestId}")
    public ItemRequestWithItemsDto getItemRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                                  @PathVariable long requestId) {
        return itemRequestService.getItemRequest(userId, requestId);
    }

    @GetMapping("all")
    public List<ItemRequestWithItemsDto> getItemRequestAll(@Min(0)
                                                           @RequestParam(name = "from", defaultValue = "0")
                                                           Integer from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "10")
                                                           Integer size,
                                                           @RequestHeader(USER_ID_HEADER) long userId) {
        int page = from/size;
        final PageRequest pageRequest =  PageRequest.of(page,size,sort);
        return itemRequestService.getItemRequestAll(pageRequest,userId);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> getItemRequestAllByUserId(@RequestHeader(USER_ID_HEADER) long userId){
        return itemRequestService.getItemRequestAllByUserId(userId);
    }

}