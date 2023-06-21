package ru.practicum.shareit.user;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDbStorageTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDbStorage userDbStorage;

    @Test
    public void testCreate() {
        User user = new User("name", "user@mail.com");

        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock ->
                invocationOnMock.getArgument(0));

        User result = userDbStorage.create(user.getName(), user.getEmail());
        assertNotNull(result);
        assertEquals(result.getName(), user.getName());
    }

    @Test
    public void testCreateFail1() {
        User user = new User("name", "");

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userDbStorage.create(user.getName(), user.getEmail());
        });
    }

    @Test
    public void testCreateFail2() {
        User user = new User("name", "wrong_mail");

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userDbStorage.create(user.getName(), user.getEmail());
        });
    }

    @Test
    public void testUpdate() {
        User user = new User("name", "user@mail.com");
        User newUser = new User("newname", "user2@mail.com");
        user.setId(1L);
        newUser.setId(1L);
        when(userRepository.save(user)).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User changedUser = userDbStorage.update(user.getId(), new UserInDto(newUser.getName(),
                newUser.getEmail()));

        assertEquals(changedUser.getName(), newUser.getName());
    }

    @Test
    public void testUpdateFail1() {
        User user = new User("name", "user@mail.com");
        User newUser = new User("", "");
        user.setId(1L);
        newUser.setId(1L);
        //when(userRepository.save(user)).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userDbStorage.update(user.getId(), new UserInDto(newUser.getName(),
                    newUser.getEmail()));
        });
    }

    @Test
    public void testGet() {
        User user = new User("name", "user@mail.com");
        user.setId(1L);
        when(userRepository.findDistinctByEmailIgnoreCase(anyString()))
                .thenReturn(user);

        User user1 = userDbStorage.get("mail");
        assertEquals(user1.getEmail(), user.getEmail());
    }

    @Test
    public void testGetFail() {
        User user = new User("name", "user@mail.com");
        user.setId(1L);
        when(userRepository.findDistinctByEmailIgnoreCase(anyString()))
                .thenReturn(null);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userDbStorage.get("mail");
        });
    }

    @Test
    public void testGet2() {
        User user = new User("name", "user@mail.com");
        user.setId(1L);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        User user1 = userDbStorage.get(user.getId());
        assertEquals(user1.getEmail(), user.getEmail());
    }

    @Test
    public void testGetFail2() {
        User user = new User("name", "user@mail.com");
        user.setId(1L);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userDbStorage.get(100L);
        });
    }

    @Test
    public void testDeleteFail() {
        User user = new User("name", "user@mail.com");
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userDbStorage.delete(100L);
        });
    }

    @Test
    public void testDelete() {
        User user = new User("name", "user@mail.com");
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> {
            userDbStorage.delete(100L);
        });
    }

    @Test
    public void testCreate2() {
        User user = new User("name", "user@mail.com");

        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock ->
                invocationOnMock.getArgument(0));

        User result = userDbStorage.create(new UserInDto(user.getName(), user.getEmail()));
        assertNotNull(result);
        assertEquals(result.getName(), user.getName());
    }

    @Test
    public void testGetAll() {
        User user = new User("name", "user@mail.com");
        user.setId(1L);
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(user));

        List<User> users = userDbStorage.getAll();
        assertNotNull(users);
        assertEquals(users.size(), 1);
    }
}

