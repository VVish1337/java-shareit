package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final Sort sort = Sort.by("start").descending();
    private Item convertItem;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        User user = checkUserExists(userId);
        convertItem = itemRepository.save(ItemMapper.dtoToItem(itemDto, user));
        return ItemMapper.itemToDto(convertItem);
    }

    @Override
    public ItemDto getItemById(long itemId, long userId) {
        convertItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        List<Comment> comments = commentRepository.findByItemId(itemId);
        if (convertItem.getOwner().getId().equals(userId)) {
            return addBookingToItem(convertItem, LocalDateTime.now(), sort, comments);
        }
        return ItemMapper.itemToDto(convertItem, null, null, comments);
    }

    @Override
    public List<ItemDto> getItemList(long userId) {
        List<Item> convertList = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
        return ItemMapper.listItemToDtoList(convertList).stream()
                .map(item -> getItemById(item.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        User user = checkUserExists(userId);
        convertItem = (ItemMapper.dtoToItem(itemDto, user));
        if (oldItem.getOwner().getId() != userId) {
            log.error("Owner not found");
            throw new NotFoundException("Owner not found");
        }
        if (convertItem.getName() != null) {
            oldItem.setName(convertItem.getName());
        }
        if (convertItem.getDescription() != null) {
            oldItem.setDescription(convertItem.getDescription());
        }
        if (convertItem.getAvailable() != null) {
            oldItem.setAvailable(convertItem.getAvailable());
        }
        itemRepository.save(oldItem);
        return ItemMapper.itemToDto(oldItem);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> foundItems = itemRepository.search(text);
        return ItemMapper.listItemToDtoList(foundItems);
    }

    @Override
    public CommentDto createComment(CreateCommentDto commentDto, Long itemId, Long userId) {
        if (commentDto.getText().isBlank()) {
            log.error("Comment text is blank");
            throw new NotFoundException("Comment text is blank");
        }
        Item item = itemRepository.findById(itemId).orElseThrow();
        User author = checkUserExists(userId);
        if (bookingRepository.findSuitableBookingsForComments(itemId, userId, LocalDateTime.now()).isEmpty()) {
            log.error("Booking items for comments not found");
            throw new ItemUnavailableException("Booking items for comments not found");
        }
        Comment comment = ItemMapper.toModelComment(commentDto, item, author);
        comment = commentRepository.save(comment);
        return ItemMapper.toCommentDto(comment);
    }

    private ItemDto addBookingToItem(Item item, LocalDateTime now, Sort sort, List<Comment> comments) {
        Booking lastBooking = bookingRepository
                .findByItemIdAndEndBefore(item.getId(), now, sort).stream()
                .findFirst().orElse(null);
        Booking nextBooking = bookingRepository
                .findByItemIdAndStartAfter(item.getId(), now, sort).stream()
                .findFirst().orElse(null);
        return ItemMapper.itemToDto(item, lastBooking, nextBooking, comments);
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}