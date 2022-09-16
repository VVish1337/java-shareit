package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestPostResponseDto createItemRequest(long userId, ItemRequestPostDto dto) {
        User user = checkUserExists(userId);
        System.out.println(dto);
//        if(dto.getDescription()==null || dto.getDescription().isEmpty()){
//            throw new ItemUnavailableException("description is empty");
//        }
        ItemRequest itemRequest = itemRequestRepository
                .save(ItemRequestMapper.toItemRequest(user, dto, LocalDateTime.now()));
        return ItemRequestMapper.toItemRequestPostResponseDto(itemRequest);
    }

    @Override
    public ItemRequestWithItemsDto getItemRequest(long userId, long requestId) {
        checkUserExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
//        if (itemRequest.getRequester().getId() != userId) {
//            log.warn("Wrong user id");
//            throw new NotFoundException("Wrong user id");
//        }
        return ItemRequestMapper.toItemRequestWithItemsDto(itemRequest,
                itemRepository.findAllByRequestId(requestId),
                LocalDateTime.now());
    }

    @Override
    public List<ItemRequestWithItemsDto> getItemRequestAll(PageRequest pageRequest, long userId) {
        checkUserExists(userId);
        Page<ItemRequest> itemRequestsPage = itemRequestRepository.findAll(pageRequest);
        List<ItemRequest> itemRequestList = itemRequestsPage.stream().collect(Collectors.toList());
        return ItemRequestMapper.toItemRequestWithItemsListDto(itemRequestList, itemRepository, LocalDateTime.now());
    }

    @Override
    public List<ItemRequestWithItemsDto> getItemRequestAllByUserId(long userId) {
        checkUserExists(userId);
        return ItemRequestMapper
                .toItemRequestWithItemsListDto(itemRequestRepository.findAllByRequesterId(userId),
                        itemRepository,
                        LocalDateTime.now());
    }

    private User checkUserExists(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}