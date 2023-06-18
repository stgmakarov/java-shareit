package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * @author Stanislav Makarov
 */
public interface UserStorage {
    User create(String name, String email);

    User get(String email);

    User get(long id);

    User update(long userId, UserInDto userInDto);

    void delete(long userId);

    User create(UserInDto userInDto);

    List<User> getAll();

}
