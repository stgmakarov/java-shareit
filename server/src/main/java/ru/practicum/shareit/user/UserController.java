package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserInDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserStorage userStorage;

    @PostMapping
    public UserOutDto create(@Valid @RequestBody UserInDto userInDto) {
        return UserMapper.toUserOutDto(userStorage.create(userInDto));
    }

    @PatchMapping("{userId}")
    public UserOutDto update(@PathVariable long userId, @Valid @RequestBody UserInDto userInDto) {
        return UserMapper.toUserOutDto(userStorage.update(userId, userInDto));
    }

    @GetMapping
    public List<UserOutDto> getAllUsers() {
        return userStorage.getAll().stream().map(UserMapper::toUserOutDto).collect(Collectors.toList());
    }

    @GetMapping("{userId}")
    public UserOutDto getUser(@PathVariable long userId) {
        return UserMapper.toUserOutDto(userStorage.get(userId));
    }

    @DeleteMapping("{userId}")
    public void delUser(@PathVariable long userId) {
        userStorage.delete(userId);
    }
}
