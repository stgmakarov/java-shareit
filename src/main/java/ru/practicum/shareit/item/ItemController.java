package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentInDto;
import ru.practicum.shareit.comment.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemInDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.dto.ItemOutDto2;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ItemOutDto create(@Valid @RequestBody ItemInDto itemInDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemMapper.toItemOutDto(itemService.create(ItemMapper.toItem(itemInDto), userId));
    }

    @PatchMapping("{itemId}")
    public ItemOutDto update(@PathVariable long itemId,
                             @RequestBody ItemInDto itemInDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemMapper.toItemOutDto(itemService.update(itemId, ItemMapper.toItem(itemInDto), userId));
    }

    @GetMapping("{itemId}")
    public ItemOutDto2 getItemById(@PathVariable long itemId,
                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getByIdDto2(itemId, userId);
    }

    @GetMapping
    public List<ItemOutDto2> getAllMyItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemOutDto> findItems(@RequestParam String text) {
        return itemService.findByText(text).stream().map(ItemMapper::toItemOutDto).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}/comment")
    public List<CommentOutDto> getComments(@PathVariable long itemId) {
        return itemService.getItemComment(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutDto addComment(@PathVariable long itemId,
                                    @RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody CommentInDto comment) {
        return CommentMapper.toCommentDto(itemService.addComment(itemId, userId, comment.getText()));
    }

}
