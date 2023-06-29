package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.ShareitTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;

    private Item item;
    private User booker;
    private User owner;
    private Booking booking;
    private Comment comment;

    @BeforeEach
    public void initialize() {
        owner = ShareitTestUtils.createEntity(new User("owner", "owner@mail.com"), userRepository);

        item = ShareitTestUtils.createEntity(new Item(0, "name", "desc", true,
                owner, null), itemRepository);

        booker = ShareitTestUtils.createEntity(new User("booker", "booker@mail.com"), userRepository);

        booking = ShareitTestUtils.createEntity(initBooking(item, booker, BookingStatus.WAITING), bookingRepository);

        comment = ShareitTestUtils.createEntity(new Comment(0, "text", item, booker, LocalDateTime.now()),
                commentRepository);
    }

    private Booking initBooking(Item item, User booker, BookingStatus status) {
        return new Booking(0, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item,
                booker,
                status
        );
    }

    @Test
    public void createCommentTest() {
        Assertions.assertNotNull(comment);
    }

    @Test
    public void findByItem_IdInTest() {
        List<Comment> comments = commentRepository.findByItem_IdIn(Collections.singletonList(item.getId()));
        Assertions.assertEquals(comments.size(), 1);
        Assertions.assertEquals(comments.get(0).getId(), comment.getId());
    }

    @Test
    public void findByItem_idTest() {
        List<Comment> comments = commentRepository.findByItem_id(item.getId());
        Assertions.assertEquals(comments.size(), 1);
        Assertions.assertEquals(comments.get(0).getId(), comment.getId());
    }

}
