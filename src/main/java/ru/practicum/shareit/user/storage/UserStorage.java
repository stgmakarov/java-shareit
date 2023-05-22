package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * @author Stanislav Makarov
 */
public interface UserStorage {
    User create(String name, String email);

    User get(String email);

    User get(long id);

    User update(long userId, User user);

    void delete(long userId);

    User create(User user);

    List<User> getAll();
}
