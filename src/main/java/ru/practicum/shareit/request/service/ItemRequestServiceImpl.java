package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.dto.ItemRequestPostResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Sort sort = Sort.by("created").descending();

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestPostResponseDto createItemRequest(long userId, ItemRequestPostDto dto) {
        User user = checkUserExists(userId);
        ItemRequest itemRequest = itemRequestRepository
                .save(ItemRequestMapper.toItemRequest(user, dto, LocalDateTime.now()));
        return ItemRequestMapper.toItemRequestPostResponseDto(itemRequest);
    }

    @Override
    public ItemRequestWithItemsDto getItemRequest(long userId, long requestId) {
        checkUserExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        return ItemRequestMapper.toItemRequestWithItemsDto(itemRequest,
                itemRepository.findAllByRequestId(requestId));
    }

    @Override
    public List<ItemRequestWithItemsDto> getItemRequestAll(int from, int size, long userId) {
        checkUserExists(userId);
        Pageable pageable = PageRequest.of(from / size, size, sort);
        return itemRequestRepository.findAll(pageable).stream()
                .filter(itemRequest -> itemRequest.getRequester().getId() != userId)
                .map(itemRequest -> ItemRequestMapper.toItemRequestWithItemsDto(itemRequest,
                        itemRepository.findAllByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithItemsDto> getItemRequestAllByUserId(long userId) {
        checkUserExists(userId);
        return itemRequestRepository.findAllByRequesterId(userId).stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestWithItemsDto(itemRequest,
                        itemRepository.findAllByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    private User checkUserExists(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}