package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDtoGw;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestControllerGw {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ItemRequestInDtoGw itemRequestInDtoGw,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("New request {}, userId={}", itemRequestInDtoGw, userId);
        return itemRequestClient.createItemRequest(itemRequestInDtoGw, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get user requests, userId={}", userId);
        return itemRequestClient.getByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get user requests {}, userId={}", requestId, userId);
        return itemRequestClient.getById(requestId, userId);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(value = "from", required = false) Long from,
                                                 @Positive @RequestParam(value = "size", required = false) Long size) {
        log.info("Get all requests, userId={}, from={}, siz{}", userId, from, size);
        return itemRequestClient.getAll(userId, from, size);
    }
}