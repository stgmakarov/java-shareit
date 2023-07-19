package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserModelTest {
    @Test
    public void testUserConstructorWithNullName() {
        long id = 1L;
        String name = null;
        String email = "john@example.com";
        User user = new User(id, name, email);
        assertEquals(id, user.getId());
        assertEquals("", user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testUserConstructorWithNullEmail() {
        long id = 1L;
        String name = "John";
        String email = null;
        User user = new User(id, name, email);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals("", user.getEmail());
    }

    @Test
    public void testUserConstructorWithNullNameAndEmail() {
        long id = 1L;
        String name = null;
        String email = null;
        User user = new User(id, name, email);
        assertEquals(id, user.getId());
        assertEquals("", user.getName());
        assertEquals("", user.getEmail());
    }

}
