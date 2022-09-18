package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto itemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public static Item dtoToItem(ItemDto itemDto, User user) {
//        return new Item(itemDto.getId(),
//                itemDto.getName(),
//                itemDto.getDescription(),
//                itemDto.getAvailable(),
//                user);
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .requestId(itemDto.getRequestId())
                .build();
    }

    public static List<ItemDto> listItemToDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
    }

    public static Comment toModelComment(CreateCommentDto dto, Item item, User user) {
        return Comment.builder()
                .commentText(dto.getText())
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getCommentText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> toCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public static ItemDto itemToDto(Item item,
                                    Booking lastBooking,
                                    Booking nextBooking,
                                    List<Comment> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(BookingMapper.bookingInItemDto(lastBooking))
                .nextBooking(BookingMapper.bookingInItemDto(nextBooking))
                .comments(toCommentDtoList(comments))
                .build();
    }

    public static ItemInRequestDto toItemInRequestDto(Item item, ItemRequest request) {
        return ItemInRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(request.getId())
                .build();
    }

    public static List<ItemInRequestDto> toItemInRequestDtoList(List<Item> items, ItemRequest request) {
        return items.stream()
                .map(item -> ItemMapper.toItemInRequestDto(item, request))
                .collect(Collectors.toList());
    }
}
