package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.ShareitTestUtils;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user1;
    private User user2;

    @BeforeEach
    public void initialize() {
        user1 = ShareitTestUtils.createEntity(new User("user", "user@mail.com"), userRepository);
        user2 = ShareitTestUtils.createEntity(new User("user2", "user2@mail.com"), userRepository);
    }

    @Test
    public void createItemTest() {
        Assertions.assertNotNull(user1);
        Assertions.assertNotNull(user2);
    }

    @Test
    public void findDistinctByEmailIgnoreCaseTest() {
        User user = userRepository.findDistinctByEmailIgnoreCase(user2.getEmail().toUpperCase());
        Assertions.assertEquals(user.getEmail(), user2.getEmail());
    }
}
