package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private Item item;
    private User itemOwner;
    private Comment comment;

    @BeforeEach
    public void beforeEach() {
        itemOwner = userRepository.save(new User(null, "itemOwner", "itemOwner@email.com"));
        item = itemRepository.save(new Item(1,
                "item",
                "description",
                true,
                itemOwner,
                null));
        comment = commentRepository.save(new Comment(1, "comment", item, itemOwner, LocalDateTime.now()));
    }

    @Test
    public void findByItemId() {
        List<Comment> result = commentRepository.findByItemId(item.getId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(comment.getCommentText(), result.get(0).getCommentText());
        assertEquals(comment.getAuthor(), itemOwner);
        assertEquals(comment.getItem(), item);
    }
}