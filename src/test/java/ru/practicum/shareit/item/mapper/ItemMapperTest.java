package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    private Item item;
    private ItemDto itemDto;
    private Comment comment;
    private User user;
    private CommentDto commentDto;
    private Booking booking;
    private ItemInRequestDto itemInRequestDto;
    private ItemRequest itemRequest;

    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "name", "user@emali.com");
        item = new Item(
                1L,
                "name",
                "description",
                true,
                user,
                1L);
        itemDto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                null,
                null,
                null,
                1L);
        comment = new Comment(1L, "comment", item, user, LocalDateTime.now());
        booking = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                item,
                user,
                BookingStatus.APPROVED);
        commentDto = new CommentDto(1L, "comment", user.getName(), comment.getCreated());
        itemInRequestDto = new ItemInRequestDto(1L,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                1L);
        itemRequest = new ItemRequest(1L, item.getDescription(), user, LocalDateTime.now());
    }

    @Test
    void itemToDto() {
        assertEquals(itemDto, ItemMapper.itemToDto(item));
    }

    @Test
    void dtoToItem() {
        Item itemTest = ItemMapper.dtoToItem(itemDto, user);
        itemTest.setId(1L);
        assertEquals(item, itemTest);
    }

    @Test
    void listItemToDtoList() {
        List<ItemDto> listDto = new ArrayList<>();
        listDto.add(itemDto);
        assertEquals(listDto, ItemMapper.listItemToDtoList(Collections.singletonList(item)));
    }

    @Test
    void toModelComment() {
        CreateCommentDto commentDto1 = new CreateCommentDto(comment.getCommentText());
        Comment testComment = ItemMapper.toModelComment(commentDto1, item, user);
        testComment.setId(1L);
        assertEquals(comment.getAuthor(), testComment.getAuthor());
        assertEquals(comment.getCommentText(), testComment.getCommentText());
        assertEquals(comment.getId(), testComment.getId());
        assertEquals(comment.getItem(), testComment.getItem());

    }

    @Test
    void toCommentDto() {
        assertEquals(commentDto.getId(), ItemMapper.toCommentDto(comment).getId());
        assertEquals(commentDto.getAuthorName(), ItemMapper.toCommentDto(comment).getAuthorName());
        assertEquals(commentDto.getText(), ItemMapper.toCommentDto(comment).getText());
    }

    @Test
    void toCommentDtoList() {
        assertEquals(Collections.singletonList(commentDto).get(0).getText(),
                ItemMapper.toCommentDtoList(Collections.singletonList(comment)).get(0).getText());
        assertEquals(Collections.singletonList(commentDto).get(0).getAuthorName(),
                ItemMapper.toCommentDtoList(Collections.singletonList(comment)).get(0).getAuthorName());
        assertEquals(Collections.singletonList(commentDto).get(0).getId(),
                ItemMapper.toCommentDtoList(Collections.singletonList(comment)).get(0).getId());
    }

    @Test
    void testItemToDto() {
        ItemDto dto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                BookingMapper.bookingInItemDto(booking),
                null,
                Collections.singletonList(commentDto),
                1L);
        ItemDto dtoTest = ItemMapper.itemToDto(item, booking, null, Collections.singletonList(comment));
        assertEquals(dto.getName(), dtoTest.getName());
        assertEquals(dto.getLastBooking().getBookerId(), dtoTest.getLastBooking().getBookerId());
        assertEquals(dto.getNextBooking(), dtoTest.getNextBooking());
    }

    @Test
    void toItemInRequestDto() {
        assertEquals(itemInRequestDto, ItemMapper.toItemInRequestDto(item,
                itemRequest));
    }

    @Test
    void toItemInRequestDtoList() {
        assertEquals(Collections.singletonList(itemInRequestDto),
                ItemMapper.toItemInRequestDtoList(Collections.singletonList(item), itemRequest));
    }
}