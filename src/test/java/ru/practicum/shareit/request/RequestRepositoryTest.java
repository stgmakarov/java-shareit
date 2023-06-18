package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.ShareitTestUtils;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private Item item;
    private User owner;
    private User requestor;
    private ItemRequest itemRequest;


    @BeforeEach
    public void initialize() {
        owner = ShareitTestUtils.createEntity(new User("owner", "owner@mail.com"), userRepository);
        requestor = ShareitTestUtils.createEntity(new User("requestor", "requestor@mail.com"), userRepository);

        item = ShareitTestUtils.createEntity(new Item(0, "name", "desc", true,
                owner, null), itemRepository);

        itemRequest = ShareitTestUtils.createEntity(
                new ItemRequest("Нужна весчь", requestor, LocalDateTime.now()), itemRequestRepository);

    }

    @Test
    public void createBookingTest() {
        Assertions.assertNotNull(itemRequest);
    }

    @Test
    public void findByRequestor_IdOrderByCreatedAscTest() {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestor_IdOrderByCreatedAsc(requestor.getId());
        Assertions.assertEquals(itemRequests.size(), 1);
        Assertions.assertEquals(itemRequests.get(0).getRequestor().getId(), requestor.getId());
    }

    @Test
    public void findByOrderByCreatedAscTest() {
        List<ItemRequest> itemRequests = itemRequestRepository.findByOrderByCreatedAsc(requestor.getId(),
                Pageable.unpaged());
        Assertions.assertEquals(itemRequests.size(), 0);
    }
}
