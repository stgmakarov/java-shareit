package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.ReqStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingDbStorage;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentDbStorage;
import ru.practicum.shareit.comment.storage.CommentStorage;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.dto.ItemOutDto2;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemDbStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    UserDbStorage userStorage;
    ItemServiceImpl itemService;
    ItemDbStorage itemStorage;
    BookingStorage bookingStorage;
    CommentStorage commentStorage;
    ItemRequestService requestService;

    User owner;
    User user;
    Item item;

    @BeforeEach
    public void init() {
        userStorage = Mockito.mock(UserDbStorage.class);
        itemStorage = Mockito.mock(ItemDbStorage.class);
        bookingStorage = Mockito.mock(BookingDbStorage.class);
        commentStorage = Mockito.mock(CommentDbStorage.class);
        requestService = Mockito.mock(ItemRequestService.class);

        itemService = new ItemServiceImpl(itemStorage,
                userStorage,
                bookingStorage,
                commentStorage,
                requestService);

        owner = new User(1L, "Owner name", "owner@mail.com");
        user = new User(3L, "User name", "user@mail.com");

        item = new Item(1L, "item", "description",
                true, owner, null);
    }

    @Test
    public void createItemTest() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(requestService.getById(anyLong(), anyLong())).thenReturn(new ItemRequest());

        when(itemStorage.create(any(Item.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Item item1 = itemService.create(
                new ItemInDto(
                        "name", "desc", true, 1L),
                user.getId());

        Assertions.assertNotNull(item1);
        Assertions.assertEquals(item1.getOwner().getId(), owner.getId());
    }

    @Test
    public void createItemFail1Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(requestService.getById(anyLong(), anyLong())).thenReturn(new ItemRequest());

        when(itemStorage.create(any(Item.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.create(
                    new ItemInDto(
                            "", "desc", true, 1L),
                    user.getId());
        });

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.create(
                    new ItemInDto(
                            "name", "", true, 1L),
                    user.getId());
        });

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.create(
                    new ItemInDto(
                            "name", "desc", null, 1L),
                    user.getId());
        });
    }

    @Test
    public void createItemFail2Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(requestService.getById(anyLong(), anyLong())).thenReturn(null);

        when(itemStorage.create(any(Item.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.create(
                    new ItemInDto(
                            "name", "desc", true, 1L),
                    user.getId());
        });
    }

    @Test
    public void updateItemTest() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);

        when(itemStorage.update(anyLong(), any(Item.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(1));

        Item item1 = itemService.update(item.getId(), new ItemInDto(
                "new name",
                "new desc",
                true,
                null), owner.getId());

        Assertions.assertNotNull(item1);
    }

    @Test
    public void updateItemFail1Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(null);

        when(itemStorage.update(anyLong(), any(Item.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(1));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.update(item.getId(), new ItemInDto(
                    "new name",
                    "new desc",
                    true,
                    null), owner.getId());
        });
    }

    @Test
    public void updateItemFail2Test() {
        when(userStorage.get(anyLong())).thenReturn(owner);
        when(itemStorage.getById(anyLong())).thenReturn(item);

        when(itemStorage.update(anyLong(), any(Item.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(1));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.update(item.getId(), new ItemInDto(
                    "new name",
                    "new desc",
                    true,
                    null), user.getId());
        });
    }

    @Test
    public void getByIdTest() {
        when(itemStorage.getById(anyLong())).thenReturn(item);

        Item item1 = itemService.getById(item.getId());

        Assertions.assertNotNull(item1);
        Assertions.assertEquals(item1.getId(), item.getId());
    }

    @Test
    public void getByIdFail1Test() {
        when(itemStorage.getById(anyLong())).thenReturn(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.getById(item.getId());
        });
    }

    @Test
    public void getByIdDto2Test() {
        when(itemStorage.getById(anyLong())).thenReturn(item);

        when(commentStorage.getCommentByItem(anyLong()))
                .thenReturn(Collections.singletonList(new Comment()));

        when(bookingStorage.getByItem(anyLong()))
                .thenReturn(Collections.singletonList(new Booking(
                        1L,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        item,
                        user,
                        BookingStatus.APPROVED
                )));

        item.setOwner(user);

        ItemOutDto2 itemOutDto2 = itemService.getByIdDto2(item.getId(), user.getId());

        Assertions.assertNotNull(itemOutDto2);
        Assertions.assertEquals(itemOutDto2.getId(), item.getId());
    }

    @Test
    public void getAllUserItemsTest() {
        when(itemStorage.getAllUserItems(anyLong())).thenReturn(Collections.singletonList(item));

        when(commentStorage.getAllCommentsByItemList(anyList()))
                .thenReturn(Collections.emptyList());

        when(bookingStorage.getAllBookingsByItemList(anyList()))
                .thenReturn(Collections.singletonList(new Booking(
                        1L,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        item,
                        user,
                        BookingStatus.APPROVED
                )));

        List<ItemOutDto2> itemOutDto2 = itemService.getAllUserItems(user.getId());

        Assertions.assertNotNull(itemOutDto2);
    }

    @Test
    public void findByTextTest() {
        when(itemStorage.findByText(anyString())).thenReturn(Collections.singletonList(item));

        List<Item> items = itemService.findByText("test");
        Assertions.assertNotNull(items);
        Assertions.assertEquals(items.size(), 1);
    }

    @Test
    public void findByTextFailTest() {
        when(itemStorage.findByText(anyString())).thenReturn(Collections.singletonList(item));

        List<Item> items = itemService.findByText("");
        Assertions.assertNotNull(items);
        Assertions.assertEquals(items.size(), 0);
    }

    @Test
    public void isItemAvailableTest() {
        when(itemStorage.isItemAvailable(anyLong())).thenReturn(true);

        boolean res = itemService.isItemAvailable(1L);
        Assertions.assertTrue(res);
    }

    @Test
    public void getItemCommentTest() {
        when(commentStorage.getCommentByItem(anyLong()))
                .thenReturn(Collections.singletonList(new Comment()));

        List<Comment> comments = itemService.getItemComment(1L);
        Assertions.assertNotNull(comments);
        Assertions.assertEquals(comments.size(), 1);
    }

    @Test
    public void addCommentTest() {
        when(userStorage.get(anyLong())).thenReturn(user);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(commentStorage.save(any(Comment.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        when(bookingStorage.getByBookerIdAndTime(
                anyLong(), any(ReqStatus.class), anyBoolean(), any(Pageable.class)))
                .thenReturn(Collections.singletonList(new Booking(
                        1L,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        item,
                        user,
                        BookingStatus.APPROVED
                )));
        String text = "text";
        Comment comment = itemService.addComment(item.getId(), user.getId(), text);

        Assertions.assertNotNull(comment);
        Assertions.assertEquals(comment.getText(), text);
    }

    @Test
    public void addCommentFail1Test() {
        when(userStorage.get(anyLong())).thenReturn(user);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(commentStorage.save(any(Comment.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        when(bookingStorage.getByBookerIdAndTime(
                anyLong(), any(ReqStatus.class), anyBoolean(), any(Pageable.class)))
                .thenReturn(Collections.singletonList(new Booking(
                        1L,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        item,
                        user,
                        BookingStatus.APPROVED
                )));
        String text = "";
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.addComment(item.getId(), user.getId(), text);
        });
    }

    @Test
    public void addCommentFail2Test() {
        when(userStorage.get(anyLong())).thenReturn(user);
        when(itemStorage.getById(anyLong())).thenReturn(item);
        when(commentStorage.save(any(Comment.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        when(bookingStorage.getByBookerIdAndTime(
                anyLong(), any(ReqStatus.class), anyBoolean(), any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        String text = "text";
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.addComment(item.getId(), user.getId(), text);
        });
    }
}
