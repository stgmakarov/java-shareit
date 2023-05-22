package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserStorage userStorage;

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        return userStorage.create(UserMapper.toUser(userDto));
    }

    @PatchMapping("{userId}")
    public User update(@PathVariable long userId, @Valid @RequestBody UserDto userDto) {
        return userStorage.update(userId, UserMapper.toUser(userDto));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable long userId) {
        return userStorage.get(userId);
    }

    @DeleteMapping("{userId}")
    public void delUser(@PathVariable long userId) {
        userStorage.delete(userId);
    }
}
