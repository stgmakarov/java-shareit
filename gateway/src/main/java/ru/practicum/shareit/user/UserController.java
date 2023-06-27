package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserInDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserInDto userInDto) {
        log.info("New user {}", userInDto);
        return userClient.create(userInDto);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> update(@PathVariable long userId, @Valid @RequestBody UserInDto userInDto) {
        log.info("Update user {}, userId={}", userInDto, userId);
        return userClient.update(userId, userInDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAll();
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info("Get user info, userId={}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delUser(@PathVariable long userId) {
        log.info("Delete user, userId={}", userId);
        return userClient.deleteUser(userId);
    }

}