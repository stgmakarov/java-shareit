package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInDtoGw;
import ru.practicum.shareit.item.dto.ItemInDtoGw;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemControllerGw {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody ItemInDtoGw itemInDtoGw, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.createItem(itemInDtoGw, userId);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                                             @RequestBody ItemInDtoGw itemInDtoGw,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.updateItem(itemId, itemInDtoGw, userId);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllMyItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get Items, userId={}", userId);
        return itemClient.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItems(@RequestParam String text) {
        log.info("Find Items, text={}", text);
        return itemClient.findByText(text);
    }

    @GetMapping("/{itemId}/comment")
    public ResponseEntity<Object> getComments(@PathVariable long itemId) {
        log.info("Get comment item={}", itemId);
        return itemClient.getItemComment(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody CommentInDtoGw comment) {
        log.info("New comment {}, item={}, userId={}", comment, itemId, userId);
        return itemClient.addComment(itemId, userId, comment.getText());
    }

}