package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemInDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ItemInDto itemInDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.createItem(itemInDto, userId);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> update(@PathVariable long itemId,
                                         @RequestBody ItemInDto itemInDto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.updateItem(itemId, itemInDto, userId);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getByIdDto2(itemId, userId);
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
                                             @RequestBody CommentInDto comment) {
        log.info("New comment {}, item={}, userId={}", comment, itemId, userId);
        return itemClient.addComment(itemId, userId, comment.getText());
    }

}