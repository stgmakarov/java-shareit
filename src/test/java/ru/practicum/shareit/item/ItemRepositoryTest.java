package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.ShareitTestUtils;

import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Item item1;
    private Item item2;
    private User owner;

    @BeforeEach
    public void initialize() {
        owner = ShareitTestUtils.createEntity(new User("owner", "owner@mail.com"), userRepository);
        User owner2 = ShareitTestUtils.createEntity(new User("owner2", "owner2@mail.com"), userRepository);

        item1 = ShareitTestUtils.createEntity(new Item(0, "name1", "description", true,
                owner, null), itemRepository);
        item2 = ShareitTestUtils.createEntity(new Item(0, "name2", "DeSCRIPTION", true,
                owner, null), itemRepository);
        Item item3 = ShareitTestUtils.createEntity(new Item(0, "name3", "___", true,
                owner2, null), itemRepository);
    }

    @Test
    public void createItemTest() {
        Assertions.assertNotNull(item1);
        Assertions.assertNotNull(item2);
    }

    @Test
    public void findAllByOwner_IdOrderByIdTest() {
        List<Item> items = itemRepository.findAllByOwner_IdOrderById(owner.getId());
        Assertions.assertEquals(items.size(), 2);
        Assertions.assertEquals(items.get(0).getId(), item1.getId());
    }

    @Test
    public void findByDescriptionLikeIgnoreCaseAndAvailableEqualsTest() {
        List<Item> items = itemRepository.findByDescriptionLikeIgnoreCaseAndAvailableEquals("Description", true);
        Assertions.assertEquals(items.size(), 2);
    }

}
